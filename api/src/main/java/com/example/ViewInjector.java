package com.example;

/**
 * 接口
 * Created by WZG on 2017/1/11.
 */

public interface ViewInjector<M> {
    void inject(M m, Object object);
}
