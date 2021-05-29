package com.gcy.bootwithutils.service.thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName FIFOMutex
 * @Description 队列锁
 * @Author Eddie
 * @Date 2021/05/29 18:48
 */
public class FIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread currentThread = Thread.currentThread();
        waiters.add(currentThread);
        // 持续阻塞
        while (waiters.peek() != currentThread || locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (currentThread.isInterrupted()) {
                wasInterrupted = true;
            }
        }
        // 是第一个入队元素且由当前线程获得资源
        waiters.remove();
        if (wasInterrupted) {
            currentThread.interrupt();
        }
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
