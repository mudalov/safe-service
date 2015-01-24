package com.mudalov.safe;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * User: mudalov
 * Date: 22/01/15
 * Time: 23:35
 */
public interface Command<T> {

    T execute();

    Future<T> queue();

    void withFallback(Callable<T> callable);

    void withFallbackValue(T value);

    void withErrorHandler(ErrorHandler errorHandler);

}
