package com.mudalov.safe.cache;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 23:38
 */
public interface Cache {

    <T> T get(String key);

    <T> void set(String key, T value);

}
