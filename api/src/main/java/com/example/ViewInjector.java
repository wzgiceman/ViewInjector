package com.example;

/**
 * Created by WZG on 2017/1/11.
 */

public interface ViewInjector<M> {
    void inject(M m, Object object);
}
