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

    private final BaseCommand<T> command;

    CommandRef(GroupExecutionContext context, BaseCommand<T> command) {
        this.context = context;
        this.command = command;
    }

    public T execute() {
        return this.context.execute(command);
    }

    public Future<T> queue() {
        return this.context.queue(command);
    }
}
