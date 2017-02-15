package com.yang.retrofit_ok_rx.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yang.retrofit_ok_rx.annotation.ParamsInfo;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    protected T mPresenter;
    protected View mRootView;
    protected int mContentViewId;

    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            onArgumentsReceive(bundle);
        }
    }

    protected abstract void onArgumentsReceive(Bundle bundle);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            if (getClass().isAnnotationPresent(ParamsInfo.class)) {
                ParamsInfo annotation = getClass().getAnnotation(ParamsInfo.class);
                mContentViewId = annotation.contentViewId();
            } else {
                throw new RuntimeException("Class must add annotations of ParamsInfo.class");
            }
            mRootView = inflater.inflate(mContentViewId, container, false);
            ButterKnife.bind(this, mRootView);
            initView(mRootView);
        }
        return mRootView;
    }

    protected abstract void initView(View view);

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    private Toast toast;

    @Override
    public void showToast(String msg) {
        if (toast != null) {
            toast.setText(msg);
        } else {
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }
}
