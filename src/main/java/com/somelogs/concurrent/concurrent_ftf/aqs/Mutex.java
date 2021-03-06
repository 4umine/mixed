package com.somelogs.concurrent.concurrent_ftf.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 通过 AQS 自定义同步组件
 *
 * @author LBG - 2017/11/8 0008
 */
public class Mutex implements Lock {

    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    /**
     * 静态内部类，继承同步器
     */
    private class Sync extends AbstractQueuedSynchronizer {

        /**
         * 当状态为0 时获取锁，并把状态改为1
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 释放锁，将状态设为0
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * 是否处于独占状态，1 表示独占
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() ==1;
        }
    }

    private static class CountTest {
        private static int count;

        private void increase() {
            count++;
        }

        private int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Mutex mutex = new Mutex();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        final CountDownLatch latch = new CountDownLatch(20);
        final CountTest count = new CountTest();
        for (int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    mutex.lock();
                    try {
                        latch.countDown();
                        for (int j = 0; j < 10000; j++) {
                            count.increase();
                        }
                    } finally {
                        if (mutex.isLocked()) {
                            mutex.unlock();
                        }
                    }
                }
            });
        }
        latch.await();
        executor.shutdown();
        System.out.println("count = " + count.getCount());
    }
}
