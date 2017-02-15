package com.yang.retrofit_ok_rx.module.news.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.base.BaseFragment;
import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;
import com.yang.retrofit_ok_rx.module.news.presenter.INewsListPresenter;
import com.yang.retrofit_ok_rx.module.news.presenter.INewsListPresenterImpl;
import com.yang.retrofit_ok_rx.module.news.ui.adapter.NewsListAdapter;
import com.yang.retrofit_ok_rx.module.news.view.INewsListView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/2/8.
 */
@ParamsInfo(contentViewId = R.layout.fragment_news_list)
public class NewsListFragment extends BaseFragment<INewsListPresenter> implements INewsListView,
        SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String id;
    private int pageNum;
    private NewsListAdapter adapter;
    private boolean isRefresh;

    public static BaseFragment getInstance(String id) {
        BaseFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onArgumentsReceive(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    protected void initView(View view) {
        mPresenter = new INewsListPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        adapter = new NewsListAdapter(getActivity(), null);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.e("onScrolled", "到底了");
                    loadNewsList(id, false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        loadNewsList(id, true);
    }

    private void loadNewsList(String id, boolean isRefresh) {
        this.isRefresh = isRefresh;
        pageNum = isRefresh ? 0 : pageNum + 20;
        Log.e("loadNewsList", "id:" + id);
        mPresenter.getNewsList(id, pageNum);
    }

    @Override
    public void loadSuccess(List<NeteastNewsSummary> data) {
        refreshLayout.setRefreshing(false);
        if (isRefresh) {
            adapter.refreshData(data);
        } else {
            adapter.loadMoreData(data);
        }
    }

    @Override
    public void loadError(String msg) {
        refreshLayout.setRefreshing(false);
    }
}
