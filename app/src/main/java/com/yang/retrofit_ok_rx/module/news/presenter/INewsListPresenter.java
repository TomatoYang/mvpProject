package com.yang.retrofit_ok_rx.module.news.presenter;

import com.yang.retrofit_ok_rx.base.BasePresenter;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface INewsListPresenter extends BasePresenter {

    void getNewsList(String id, int pageNum);

}
