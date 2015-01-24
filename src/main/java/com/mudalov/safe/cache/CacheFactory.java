package com.mudalov.safe.cache;

/**
 * Provides cache implementation
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 00:24
 */
public interface CacheFactory {

    /**
     * Provide cache instance to be shared by specified Commands Group
     *
     * @param groupName
     */
    Cache getCache(String groupName);

    /**
     * Provide common cache instance
     *
     */
    Cache getCache();

}
