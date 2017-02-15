package com.yang.retrofit_ok_rx.module.news.presenter;

import com.yang.retrofit_ok_rx.base.BasePresenterImpl;
import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;
import com.yang.retrofit_ok_rx.module.news.model.INewsListInterceptor;
import com.yang.retrofit_ok_rx.module.news.model.INewsListInterceptorImpl;
import com.yang.retrofit_ok_rx.module.news.view.INewsListView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public class INewsListPresenterImpl extends BasePresenterImpl<INewsListView, List<NeteastNewsSummary>>
        implements INewsListPresenter {

    private INewsListInterceptor<List<NeteastNewsSummary>> interceptor;

    public INewsListPresenterImpl(INewsListView view) {
        super(view);
        interceptor = new INewsListInterceptorImpl();
    }

    @Override
    public void getNewsList(String id, int pageNum) {
        mSubscription = interceptor.getNewsList(this, id, pageNum);
    }

    @Override
    public void requestSuccess(List<NeteastNewsSummary> data) {
        getView().loadSuccess(data);
    }

    @Override
    public void requestError(String msg) {
        getView().loadError(msg);
    }
}
