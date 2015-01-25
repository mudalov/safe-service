package com.mudalov.safe.impl;

import com.mudalov.safe.Command;

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
        return this.context.queue(this);
    }

    abstract Callable<T> action();

    protected void checkContextReady() {
        if (context == null) {
            throw new IllegalStateException("Execution context is not provided");
        }
    }

}
