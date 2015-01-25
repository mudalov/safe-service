package com.mudalov.safe.impl;

import com.mudalov.safe.cache.Cache;
import com.mudalov.safe.cache.CacheFactory;
import com.mudalov.safe.config.Configuration;
import com.mudalov.safe.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Responsible for executing commands from one dependency group
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 00:52
 */
public class GroupExecutionContext {

    private static final Logger log = LoggerFactory.getLogger(GroupExecutionContext.class);

    private final Integer nThreads;

    private final Integer maxWorkQueueSize;

    private final Cache cache;

    private final ExecutorService executorService;

    private final BlockingQueue<Runnable> workQueue;

    private final Configuration config;

    private final String groupName;

    public GroupExecutionContext(String groupName) {
        this.groupName = groupName;
        config = Configuration.root().forGroup(groupName);
        nThreads = config.getThreadsPerGroup();
        maxWorkQueueSize = config.getMaxWorkQueueSize();
        CacheFactory cacheFactory = ReflectionUtils.createInstanceSilently(config.getCacheFactory());
        cache = cacheFactory.getCache(groupName);
        workQueue = new LinkedBlockingQueue<Runnable>(maxWorkQueueSize > 0 ? maxWorkQueueSize : Integer.MAX_VALUE);
        this.executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                workQueue);
    }

    public <T> T execute(BaseCommand<T> command) {
        Future<T> actionFuture = this.executorService.submit(command.action());
        try {
            return actionFuture.get(config.getTimeOut(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            processError(command, e);
        } finally {
            actionFuture.cancel(true);
        }
        return command.fallback();
    }

    private void processError(BaseCommand command, Exception cause) {
        log.debug(this.getGroupName() + " - Command Execution failed", cause);
        command.onError(cause);
    }

    public String getGroupName() {
        return groupName;
    }

    public <T> Future<T> queue(BaseCommand<T> command) {
        // TODO
        return null;
    }


}
