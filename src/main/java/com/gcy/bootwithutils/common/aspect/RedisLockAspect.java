package com.gcy.bootwithutils.common.aspect;



import com.gcy.bootwithutils.common.annotation.RedisLock;
import com.gcy.bootwithutils.common.constants.RedisLockTypeEnum;
import com.gcy.bootwithutils.common.dto.RedisLockHolder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLockAspect
 * @Description redis分布式锁 切面主逻辑
 * @Author Eddie
 * @Date 2021/03/21 13:11
 */
@Aspect
@Component
@Order(1)
public class RedisLockAspect {

    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("redis-lock-pool").daemon(true).build());

    private static ConcurrentLinkedQueue<RedisLockHolder> holderList = new ConcurrentLinkedQueue();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.gcy.bootwithutils.common.annotation.RedisLock)")
    public void redisLock(){}

    {
     SCHEDULER.scheduleAtFixedRate(() -> {
         Iterator<RedisLockHolder> iterator = holderList.iterator();
         while(iterator.hasNext()) {
             RedisLockHolder redisLockHolder = iterator.next();
            /* System.out.println("扫描到资源锁\nkey: \t" + redisLockHolder.getBusinessKey()
                     + "\n锁定时间: \t" + redisLockHolder.getLockTime()
                     + "\n尝试获取次数: \t" + redisLockHolder.getCurrentCount()
                     + "\n获取次数上限: \t" + redisLockHolder.getTryCount()
                     + "\n上次修改时间: \t" + redisLockHolder.getLastModifyTime()
                     + "\n当前线程: \t" + redisLockHolder.getCurrentThread());*/
              Optional<String> item = Optional.ofNullable(redisTemplate.opsForValue().get(redisLockHolder.getBusinessKey()));
              if (!item.isPresent()) {
                  iterator.remove();
                  continue;
              }

              // 重试次数 > 上限次数  打断线程，终止访问
              if (redisLockHolder.getCurrentCount() > redisLockHolder.getTryCount()) {
                  redisLockHolder.getCurrentThread().interrupt();
                  iterator.remove();
                  continue;
              }

              // 续时

             long currTime = System.currentTimeMillis() / 1000;
              // 上次变动的时间戳 + 续时的界限已经小于当前时间（即将过期）
              if (redisLockHolder.getLastModifyTime() + redisLockHolder.getModifyPeriod() <= currTime) {
                  System.out.println("key :\t" + redisLockHolder.getBusinessKey() + "续时，当前时间\t" + currTime + "\t>=\t" + redisLockHolder.getLastModifyTime() + "\t+\t" + redisLockHolder.getModifyPeriod());
                  redisLockHolder.setLastModifyTime(currTime);
                redisTemplate.expire(redisLockHolder.getBusinessKey(), redisLockHolder.getLockTime(), TimeUnit.SECONDS);
                redisLockHolder.setCurrentCount(redisLockHolder.getCurrentCount() + 1);
              }






         }
     }, 0, 2, TimeUnit.SECONDS);
    }

    @Around(value = "redisLock()")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = resolveMethod(joinPoint);
        RedisLock annotation = method.getAnnotation(RedisLock.class);
        RedisLockTypeEnum businessLockType = annotation.typeEnum();
        Object[] params = joinPoint.getArgs();
        String ukStr = params[annotation.lockFailed()].toString();
        String businessKey = businessLockType.getUniqueKey(ukStr);
        String uniqueValue = UUID.randomUUID().toString();
        Object result = null;

        try {
            boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(businessKey, uniqueValue, Duration.ofMinutes(1L));
            if (!isSuccess) {
                throw new Exception("resources are locked");
            }
            redisTemplate.expire(businessKey, annotation.lockTime(), TimeUnit.SECONDS);
            Thread currentThread = Thread.currentThread();
            long currTime = System.currentTimeMillis() / 1000;
            RedisLockHolder redisLockHolder = new RedisLockHolder(businessKey, annotation.lockTime(),
                    currTime , currentThread, annotation.tryCount());
            holderList.add(redisLockHolder);
            System.out.println("lock \t" + businessKey + "\t当前时间\t" + currTime + "\t超时时间\t" + (currTime + redisLockHolder.getModifyPeriod()));
            result = joinPoint.proceed();
            if (currentThread.isInterrupted()) {
                throw new InterruptedException("You had been interrupted");
            }
        } catch (Exception e) {
            System.out.println("encounter an error " + e);
        } finally {
            releaseRedisLock(businessKey, Thread.currentThread());
        }
        return result;
    }




    private Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Optional<Method> method = Optional.ofNullable(getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes()));
        if (method.isPresent()) {
            return method.get();
        }
        throw new IllegalStateException("get target method fail\t" + signature.getMethod().getName());
    }

    public static Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Optional<Class<?>> superClass = Optional.ofNullable(clazz.getSuperclass());
            return superClass.map(aClass -> getDeclaredMethodFor(aClass, name, parameterTypes)).orElse(null);
        }
    }

    /*
     *@Method releaseRedisLock
     *@Params [businessKey, currentThread]
     *@Description 释放资源
     *@Author Eddie
     *@Date 2021/03/26 15:00
     */
    public void releaseRedisLock(String businessKey, Thread currentThread) {
        try {
            RedisLockHolder redisLockHolder =  holderList.stream().filter(h -> businessKey.equals(h.getBusinessKey())).findFirst().orElse(null);
            if (redisLockHolder != null && currentThread.equals(redisLockHolder.getCurrentThread())) {
                redisTemplate.delete(businessKey);
                System.out.println("release the lock " + businessKey);
            }
        } catch (Exception e) {
            System.out.println("release the lock encounter an error " + e);
        }
    }


}
