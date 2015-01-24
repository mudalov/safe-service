package com.mudalov.safe.impl;

import com.mudalov.safe.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * User: mudalov
 * Date: 22/01/15
 * Time: 23:57
 */
public class SafeCommands {

    private static final Logger log = LoggerFactory.getLogger(SafeCommands.class);

    private String group = "DefaultGroup";

    private Integer nThreads = 10;

    private Integer maxWorkQueueSize = -1;

    private ExecutorService executorService;

    private BlockingQueue<Runnable> workQueue;

    private SafeCommands() {
    }

    private void initExecutor() {
        workQueue = new LinkedBlockingQueue<Runnable>(maxWorkQueueSize > 0 ? maxWorkQueueSize : Integer.MAX_VALUE);
        this.executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                workQueue);
    }

    public <T> Future<T> execute(Callable<T> callable) {
        return this.executorService.submit(callable);
    }

    public <T> Command<T> create(Class<? extends Command<T>> clazz, String group) {
        return null;
    }

    public static class Builder {

        SafeCommands instance = new SafeCommands();

        public Builder withThreadsNumber(Integer nThreads) {
            instance.nThreads = nThreads;
            return this;
        }

        public Builder withGroupName(String group) {
            instance.group = group;
            return this;
        }

        public Builder withWorkQueueSize(Integer size) {
            instance.maxWorkQueueSize = size;
            return this;
        }

    }


}
