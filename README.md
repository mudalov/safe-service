# Safe Service Calls (safe-service)

Library for improving your server's fault tolerance

## Overview

Modern Server applications can have a lot of dependencies on 3rd party client libraries, REST/Web Serices, etc. Very often these dependencies act as a "black boxes" for your system. It can lead to disasters for servers with common "thread per user request" architecture - if some services become not responsive, then with a high load there is a possibility that all server's threads will be in a waiting sets of this services and whole system become unresponsive. Following fault tolerance items implemented to avoid such situations:

- "per-dependency" thread pulls to de-couple the service from the whole syste
- Network time outs for not responsive services
- Error handling and fallback procedures for rejected/failed requests
- Use of cached values from previous successful calls

## Essential Information

### License

See LICENSE file

### Binaries 

All binaries will be published to Maven Central.

    <dependency>
        <groupId>com.mudalov</groupId>
        <artifactId>safe-service</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

### Usage of the library

#### Configuration

To change configation you need to have *safe-service.conf* file in your classpath. Configuration is stored in a [HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md) format. Here is the list of supported properties and their default values:


    # Number of thread allocated for Dependency group
    
    threadsPerGroup : 5
    
    # Max size of the working queue per Dependency group, -1 means "unlimited"
    
    maxWorkQueueSize : -1
    
    # Time Out (ms) to wait before rejecting the request
    
    timeOut : 1000
    
    # Custom Cache Factory implementation (instead of standard, based on EhCache)
    
    cacheFactory : "com.mudalov.safe.cache.ehcache.EhCacheFactory"
    
    # Each property can be overwritten for specified Dependency group,
    # e.g. to increase threads pull size for high priority service
    
    userInfoService {
        threadsPerGroup : 12
    }
    
    # or to increase time out for slow service
    
    slowService {
        timeOut : 1500
    }
    
#### API Example

Here is a simple example of executing action as a part of "userInfoService" dependency group, cached user information used in case of failure: 

        User user = SafeCommands.create(new AbstractCachedCommand<User>() {
            @Override
            public String getKey() {
                // key to be used for cache, each dependency group stored in a separate cache region
                return userId.toString();
            }
            @Override
            public void onError(Exception cause) {
                log.error("Failed to get user, using cached value", e);
            }
            @Override
            public User action() throws Exception {
                return userService.getUser(userId);
            }
        }, "userInfoService").execute();

Please reference to unit tests for more detailed examples.
