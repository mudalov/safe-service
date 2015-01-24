package com.mudalov.safe;


public interface CachedCommand<T> extends Command<T> {

    public String getKey();

}
