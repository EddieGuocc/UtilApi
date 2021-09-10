package com.gcy.bootwithutils.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadUtil {

    private static volatile ExecutorService executorPool;

    static {
        initExecutor();
    }

    public static Future<?> submit(Runnable task) {
        return executorPool.submit(task);
    }


    /*
     * @description:  初始化线程池
     * @param: []
     * @return: void
     * @author Eddie Guo
     * @date: 2021/9/7 17:43
     */
    private static void initExecutor() {
        if (executorPool == null) {
            synchronized (ThreadUtil.class) {
                if (executorPool == null) {
                    executorPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100, 1,
                            TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000), new DefaultThreadFactory());
                }
            }
        }
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup threadGroup;
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager sm = System.getSecurityManager();
            this.threadGroup = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "pool-" + DefaultThreadFactory.poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.threadGroup, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
