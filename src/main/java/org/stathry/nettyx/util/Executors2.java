package org.stathry.nettyx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executors2
 * Created by dongdaiming on 2018-06-05 16:49
 */
public class Executors2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Executors2.class);

    private static final AtomicInteger POOL_COUNT = new AtomicInteger();
    private static final String DEFAULT_THREAD_NAME_PREFIX = "exec";

    private static final int DEFAULT_CORE_POOL_SIZE = 8;
    private static final int DEFAULT_MAX_POOL_SIZE = 16;
    private static final int DEFAULT_WORK_QUEUE_SIZE = 1000;
    private static final long DEFAULT_ALIVE_TIME = 60;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
    private static final RejectedExecutionHandler DEFAULT_HANDLER = new LogPolicy();

    public static ExecutorService newCustomExecutorService(int corePoolSize, int maxPoolSize, long keepAliveTime,
                                                           TimeUnit unit, int workQueueSize, String threadNamePrefix, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>(workQueueSize),
                new CustomizableThreadFactory(threadNamePrefix), handler);
    }

    public static ExecutorService newDefaultExecutorService() {
        String threadNamePrefix = DEFAULT_THREAD_NAME_PREFIX + POOL_COUNT.incrementAndGet() + "-";
        return newCustomExecutorService(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT,
                DEFAULT_WORK_QUEUE_SIZE, threadNamePrefix, DEFAULT_HANDLER);
    }

    public static ExecutorService newDefaultExecutorService(String threadNamePrefix) {
        return newCustomExecutorService(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT,
                DEFAULT_WORK_QUEUE_SIZE, threadNamePrefix, DEFAULT_HANDLER);
    }

    public static ExecutorService newDefaultExecutorService(String threadNamePrefix, int corePoolSize, int maxPoolSize) {
        return newCustomExecutorService(corePoolSize, maxPoolSize, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT,
                DEFAULT_WORK_QUEUE_SIZE, threadNamePrefix, DEFAULT_HANDLER);
    }

    public static ExecutorService newDefaultExecutorService(String threadNamePrefix, int corePoolSize, int maxPoolSize, int queueSize) {
        return newCustomExecutorService(corePoolSize, maxPoolSize, DEFAULT_ALIVE_TIME, DEFAULT_TIME_UNIT,
                queueSize, threadNamePrefix, DEFAULT_HANDLER);
    }

    public static class LogPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            ThreadFactory factory = executor.getThreadFactory();
            String threadNamePrefix = "";
            if(factory.getClass() == CustomizableThreadFactory.class) {
                CustomizableThreadFactory factory1 = (CustomizableThreadFactory)factory;
                threadNamePrefix = factory1.getThreadNamePrefix();
            }
            LOGGER.error("threadNamePrefix {}, corePoolSize {}, maxPoolSize {}, workQueueSize {}, rejected task {}." ,
                    new Object[]{threadNamePrefix, executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getQueue().size(), r.toString()});
        }
    }
}
