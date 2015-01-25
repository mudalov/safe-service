package com.mudalov.safe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: mudalov
 * Date: 25/01/15
 * Time: 00:56
 */
public class ReflectionUtils {

    private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T createInstanceSilently(String className) {
        try {
            return createInstanceSilently((Class<T>)Class.forName(className));
        } catch (Exception e) {
            log.error("Unable to create instance for " + className, e);
            return null;
        }
    }

    public static <T> T createInstanceSilently(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (Exception e) {
            log.error("Unable to create instance for " + tClass.getName(), e);
            return null;
        }
    }
}
