package com.mudalov.safe.impl;

import com.mudalov.safe.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Base command, main building block for execution items.
 * All commands must override this class for execution.
 *
 * User: mudalov
 * Date: 23/01/15
 * Time: 01:36
 */
public abstract class BaseCommand<T> implements Command<T> {

    private GroupExecutionContext context;

    protected void setContext(GroupExecutionContext context) {
        this.context = context;
    }

    protected GroupExecutionContext getContext() {
        return this.context;
    }

    protected BaseCommand() {
    }

    @Override
    public T execute() {
        checkContextReady();
        return this.context.execute(this);
    }

    @Override
    public Future<T> queue() {
        checkContextReady();
        return this.context.queue(this);
    }

    /**
     * Action to execute, called asynchronously in separate thread
     *
     */
    abstract Callable<T> action();

    /**
     * Result in case of task failure, called from client thread
     *
     * @return
     */
    abstract T fallback();

    /**
     * Error callback to be called if task execution can't be completed,
     * called in a client thread before using fallback procedure
     *
     * @param cause error cause, one of following exceptions:
     *              TimeOutException - in case of time out
     *              ExecutionException - in case of error in main action
     *              InterruptedException - if execution thread is interrupted
     *              RejectedExecutionException - if task is rejected due to service shutdown or capacity limitations
     */
    public void onError(Exception cause) {
        // let client code to deal with that
    }

    protected void checkContextReady() {
        if (context == null) {
            throw new IllegalStateException("Execution context is not provided");
        }
    }

}
