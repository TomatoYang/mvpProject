package com.yang.retrofit_ok_rx.module.photo.presenter;

import com.yang.retrofit_ok_rx.base.BasePresenter;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface IPhotoListPresenter extends BasePresenter {

    void getPhotos(String id, int pageNum);

}
