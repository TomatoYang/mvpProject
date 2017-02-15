package com.yang.retrofit_ok_rx.module.photo.view;

import com.yang.retrofit_ok_rx.base.BaseView;
import com.yang.retrofit_ok_rx.bean.PhotoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface IPhotoListView extends BaseView {

    void onLoadSuccess(List<PhotoModel.PictureBody> list);

    void onLoadError(String msg);
}
