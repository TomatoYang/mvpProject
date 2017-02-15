package com.yang.retrofit_ok_rx.module.photo.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.base.BaseFragment;
import com.yang.retrofit_ok_rx.bean.PhotoModel;
import com.yang.retrofit_ok_rx.module.photo.presenter.IPhotoListPresenter;
import com.yang.retrofit_ok_rx.module.photo.presenter.IPhotoListPresenterImpl;
import com.yang.retrofit_ok_rx.module.photo.ui.adapter.PhotoListAdapter;
import com.yang.retrofit_ok_rx.module.photo.view.IPhotoListView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/2/7.
 */
@ParamsInfo(contentViewId = R.layout.fragment_photo_list)
public class PhotoListFragment extends BaseFragment<IPhotoListPresenter>
        implements IPhotoListView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String id;
    private int pageNum;
    private boolean isRefresh;
    private PhotoListAdapter adapter;

    public static BaseFragment getInstance(String id) {
        PhotoListFragment fragment = new PhotoListFragment();
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
        refreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        mPresenter = new IPhotoListPresenterImpl(this);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });

    }

    private void loadPhotos(String id, boolean isRefresh) {
        this.isRefresh = isRefresh;
//        pageNum = isRefresh ? 1 : pageNum++;
        if (isRefresh) {
            pageNum = 1;
        } else {
            pageNum++;
        }
        Log.e("请求page", pageNum + "");
        mPresenter.getPhotos(id, pageNum);
    }

    @Override
    public void onLoadSuccess(List<PhotoModel.PictureBody> list) {
        refreshLayout.setRefreshing(false);
        if (isRefresh) {
            adapter.refreshData(list);
        } else {
            adapter.loadMoreData(list);
        }
    }

    @Override
    public void onLoadError(String msg) {
        refreshLayout.setRefreshing(false);
    }

    private void initRecyclerView() {
        adapter = new PhotoListAdapter(getContext(), null);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.e("onScrolled", "到底了");
                    loadPhotos(id, false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadPhotos(id, true);
    }
}
