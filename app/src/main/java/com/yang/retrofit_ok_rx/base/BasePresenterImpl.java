package com.yang.retrofit_ok_rx.base;

import android.util.Log;

import com.yang.retrofit_ok_rx.http.HttpCallback;

import rx.Subscription;

/**
 * 当后台返回的数据格式统一时可以省略V泛型的声明,直接采用BaseBean管理
 */
public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter,HttpCallback<V> {

    private static final String TAG = "BasePresenterImpl";
    /**
     * 主要用于界面销毁时注销rx的绑定
     */
    protected Subscription mSubscription;
    protected T mView;

    public BasePresenterImpl(T view) {
        this.mView = view;
    }

    public T getView() {
        return mView;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }

    @Override
    public void beforeRequest() {
        Log.e(TAG, "beforeRequest");
        mView.showDialog();
    }

    @Override
    public void requestError(String msg) {
        Log.e("requestError", msg + "");
        mView.showToast(msg);
        mView.hideDialog();
    }

    @Override
    public void requestComplete() {
        Log.e(TAG, "requestComplete");
        mView.hideDialog();
    }

    @Override
    public void requestSuccess(V data) {

    }
}
