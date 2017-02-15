package com.yang.retrofit_ok_rx.module.news.model;

import com.yang.retrofit_ok_rx.http.HttpCallback;

import rx.Subscription;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface INewsListInterceptor<T> {

    Subscription getNewsList(HttpCallback<T> callback, String id, int startPage);

}
