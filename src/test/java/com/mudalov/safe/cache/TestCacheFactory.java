package com.mudalov.safe.cache;


import java.util.HashMap;
import java.util.Map;

/**
 * Test Cache Factory, returns CacheValue constant for each existing key
 *
 */
public class TestCacheFactory implements CacheFactory {

    public static final String CacheValue = "TestCachedValue";

    private static Cache instance = new TestCache();

    static class TestCache implements Cache {

        private final Map<String, Object> cache = new HashMap<String, Object>();

        @Override
        public <T> T get(String key) {
            return (T) cache.get(key);
        }

        @Override
        public <T> void set(String key, T value) {
            cache.put(key, CacheValue);
        }
    }

    @Override
    public Cache getCache(String groupName) {
        return instance;
    }

    @Override
    public Cache getCache() {
        return instance;
    }

}
