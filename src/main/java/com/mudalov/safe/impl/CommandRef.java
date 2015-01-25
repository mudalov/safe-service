package com.mudalov.safe.impl;

import java.util.concurrent.Future;

/**
 * Separates execution logic from the commands creation
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 21:15
 */
public class CommandRef<T> {

    private final GroupExecutionContext context;

    private final AbstractCommand<T> command;

    CommandRef(GroupExecutionContext context, AbstractCommand<T> command) {
        this.context = context;
        this.command = command;
    }

    /**
     * Submits command for execution, perform error handling and fall back logic if required
     *
     */
    public T execute() {
        return this.context.execute(command);
    }

    /**
     * Submits command to corresponding dependency group, no additional logic is performed.
     * Should only used in cases when custom processing is required
     *
     * @throws java.util.concurrent.RejectedExecutionException in case of capacity issues for corresponding group
     */
    public Future<T> queue() {
        return this.context.queue(command);
    }
}
