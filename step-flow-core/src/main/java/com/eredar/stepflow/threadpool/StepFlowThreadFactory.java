package com.eredar.stepflow.threadpool;

import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class StepFlowThreadFactory implements ThreadFactory {

    // 线程前缀
    private final String threadNamePrefix;
    // 线程计数器，永远增加，即使之前创造的线程被销毁
    private final AtomicInteger threadCount = new AtomicInteger();
    // 线程优先级
    @Setter
    private int threadPriority = Thread.NORM_PRIORITY;
    // 是否是守护线程
    @Setter
    private boolean daemon = false;

    public StepFlowThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return createThread(runnable);
    }

    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(null, runnable, this.nextThreadName());
        thread.setPriority(this.threadPriority);
        thread.setDaemon(this.daemon);
        return thread;
    }

    private String nextThreadName() {
        return this.threadNamePrefix + this.threadCount.incrementAndGet();
    }
}
