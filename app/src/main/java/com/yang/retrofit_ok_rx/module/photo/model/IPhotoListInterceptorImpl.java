package com.yang.retrofit_ok_rx.module.photo.model;

import com.yang.retrofit_ok_rx.bean.PhotoModel;
import com.yang.retrofit_ok_rx.http.HttpCallback;
import com.yang.retrofit_ok_rx.http.HttpResponse;
import com.yang.retrofit_ok_rx.http.RetrofitManager;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/2/7.
 */
public class IPhotoListInterceptorImpl implements IPhotoListInterceptor<HttpResponse<PhotoModel>> {

    @Override
    public Subscription getPhotoList(final HttpCallback<HttpResponse<PhotoModel>> callback, String id, int
            page) {
        return RetrofitManager.getInstance()
                .getPhotoListObservable(id, page)
                .subscribe(new Subscriber<HttpResponse<PhotoModel>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(e.toString());
                    }

                    @Override
                    public void onNext(HttpResponse<PhotoModel> response) {
                        callback.requestSuccess(response);
                    }
                });
    }
}
