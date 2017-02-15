package com.yang.retrofit_ok_rx.module.photo.model;

import com.yang.retrofit_ok_rx.http.HttpCallback;

import rx.Subscription;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface IPhotoListInterceptor<T> {

    Subscription getPhotoList(HttpCallback<T> callback, String id, int page);

}
