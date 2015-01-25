package com.mudalov.safe.impl;

import com.mudalov.safe.cache.Cache;
import com.mudalov.safe.cache.CacheFactory;
import com.mudalov.safe.config.Configuration;
import com.mudalov.safe.util.ReflectionUtils;

import java.util.concurrent.*;

/**
 * Responsible for executing commands from one dependency group
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 00:52
 */
public class GroupExecutionContext {

    private final Integer nThreads;

    private final Integer maxWorkQueueSize;

    private final Cache cache;

    private final ExecutorService executorService;

    private final BlockingQueue<Runnable> workQueue;

    public GroupExecutionContext(String groupName) {
        Configuration configuration = Configuration.root().forGroup(groupName);
        nThreads = configuration.getThreadsPerGroup();
        maxWorkQueueSize = configuration.getMaxWorkQueueSize();
        CacheFactory cacheFactory = ReflectionUtils.createInstanceSilently(configuration.getCacheFactory());
        cache = cacheFactory.getCache(groupName);
        workQueue = new LinkedBlockingQueue<Runnable>(maxWorkQueueSize > 0 ? maxWorkQueueSize : Integer.MAX_VALUE);
        this.executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                workQueue);
    }

    public <T> T execute(BaseCommand<T> command) {
        // TODO
        return null;
    }

    public <T> Future<T> queue(BaseCommand<T> command) {
        // TODO
        return null;
    }


}
