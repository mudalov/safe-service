package com.mudalov.safe.impl;

import com.mudalov.safe.Command;
import com.mudalov.safe.ErrorHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * User: mudalov
 * Date: 23/01/15
 * Time: 01:36
 */
public class BaseCommand<T> implements Command<T> {

    private final SafeCommands executor;

    private final Callable<T> callable;

    private Callable<T> fallback;

    public BaseCommand(SafeCommands executor, Callable<T> callable) {
        this.executor = executor;
        this.callable = callable;
    }

    @Override
    public T execute() {
        return null;
    }

    @Override
    public Future<T> queue() {
        return null;
    }

    @Override
    public void withFallback(Callable<T> callable) {
        this.fallback = callable;
    }

    @Override
    public void withFallbackValue(T value) {

    }

    @Override
    public void withErrorHandler(ErrorHandler errorHandler) {

    }


}
