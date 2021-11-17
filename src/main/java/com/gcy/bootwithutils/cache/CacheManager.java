package com.gcy.bootwithutils.cache;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * @description: 缓存
 * @author Eddie Guo
 * @date: 2021/11/17 14:33
 */
@Slf4j
public class CacheManager {

    private final static RemovalListener<String, Object> LISTENER = new RemovalListener<String, Object>() {
        @Override
        public void onRemoval(RemovalNotification<String, Object> notification) {
            System.out.println("delete key-value in cache " + notification.getKey());
        }
    };

    /*
     *  使用google开源java类库中的GuavaCache，基于ConcurrentHashMap实现，写入加锁，读取不加锁
     */
    private final static Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .maximumSize(50) // 指定最大容量
            .initialCapacity(10) // 设置缓存初始化大小，开发时调研清楚
            .concurrencyLevel(5) // 并发数，即同时有5个线程写入cache
            .expireAfterWrite(5, TimeUnit.SECONDS) // 存活时间
            // .removalListener(LISTENER) 指定清除监听器 清除元素时候进行回调 但是监听器和清除的线程是同步执行的，如果监听器耗时较长，会阻塞清除的线程
            .removalListener(RemovalListeners.asynchronous(LISTENER, Executors.newSingleThreadExecutor())) // 使用异步监听
            .build();

    /*
     * @description: 旨在测试guava cache 的清除机制并不是启动线程对所有value进行监听，而是在写入数据时候进行过期数据的筛查与清除
     * @param: []
     * @return: void
     * @author Eddie Guo
     * @date: 2021/11/17 16:30
     */
    public void testCacheEvict() throws InterruptedException {
        new Thread(() -> {
            while (true) {
                log.info("cache size {}", CACHE.size());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        CACHE.put("1", "A");
        log.info("cache put first value");
        log.info("cache contains first value {}", CACHE.getIfPresent("1"));
        Thread.sleep(8000);
        CACHE.put("2", "B");
        log.info("cache put second value");
        log.info("cache contains second value {}", CACHE.getIfPresent("2"));
        Thread.sleep(8000);
        log.info("after put second value, cache contain first value {}", CACHE.getIfPresent("1"));
        Thread.sleep(3000);
        log.info("cache contain second value {}", CACHE.getIfPresent("2"));

        CACHE.put("3", "C");
        Thread.sleep(1000);
        CACHE.invalidate("3");
    }

    public static void main(String[] args) throws InterruptedException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.testCacheEvict();;
    }
}
