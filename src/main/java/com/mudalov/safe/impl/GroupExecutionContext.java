package com.mudalov.safe.impl;

import com.mudalov.safe.CachedCommand;
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

    public <T> T execute(final AbstractCommand<T> command) {
        try {
            Future<T> actionFuture = submitCommand(command);
            try {
                T result = actionFuture.get(config.getTimeOut(), TimeUnit.MILLISECONDS);
                if (command instanceof CachedCommand) {
                    updateCache((CachedCommand<T>) command, result);
                }
                return result;
            } catch (Exception e) {
                processError(command, e);
            } finally {
                actionFuture.cancel(true);
            }
        } catch (RejectedExecutionException e) {
            processError(command, e);
        }
        return command.fallback();
    }

    private <T> void updateCache(CachedCommand<T> command, T result) {
        this.cache.set(key(command), result);
    }

    private <T> Future<T> submitCommand(final AbstractCommand<T> command) {
        return this.executorService.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return command.action();
            }
        });
    }

    private void processError(AbstractCommand command, Exception cause) {
        log.debug(this.getGroupName() + " - Command Execution failed", cause);
        command.onError(cause);
    }

    public String getGroupName() {
        return groupName;
    }

    public <T> Future<T> queue(AbstractCommand<T> command) {
        return submitCommand(command);
    }


    public <T> T getCachedValue(CachedCommand<T> command) {
        return this.cache.get(key(command));
    }

    private <T> String key(CachedCommand<T> command) {
        return this.groupName + "." + command.getKey();
    }
}
