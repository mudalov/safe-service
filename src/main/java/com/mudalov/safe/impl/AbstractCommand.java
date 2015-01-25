package com.mudalov.safe.impl;

import com.mudalov.safe.Command;

/**
 * Base command, main building block for execution items.
 * All commands must override this class for execution.
 *
 * User: mudalov
 * Date: 23/01/15
 * Time: 01:36
 */
public abstract class AbstractCommand<T> implements Command<T> {

    private GroupExecutionContext context;

    void setContext(GroupExecutionContext context) {
        this.context = context;
    }

    protected GroupExecutionContext getContext() {
        return this.context;
    }

    @Override
    public T fallback() {return null;};

    /**
     * Error callback to be called if task execution can't be completed,
     * called in a client thread before using fallback procedure
     *
     * @param cause error cause, one of following exceptions:
     *              @see java.util.concurrent.TimeoutException - in case of time out
     *              @see java.util.concurrent.ExecutionException - in case of error in main action
     *              @see InterruptedException - if execution thread is interrupted
     *              @see java.util.concurrent.RejectedExecutionException - if task is rejected due to service shutdown or capacity limitations
     */
    public void onError(Exception cause) {
        // let client code to deal with that
    }

}
