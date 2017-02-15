package com.yang.retrofit_ok_rx.module.news.view;

import com.yang.retrofit_ok_rx.base.BaseView;
import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface INewsListView extends BaseView {
    void loadSuccess(List<NeteastNewsSummary> data);

    void loadError(String msg);
}
