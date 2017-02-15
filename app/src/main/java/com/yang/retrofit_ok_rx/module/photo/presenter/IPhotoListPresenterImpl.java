package com.yang.retrofit_ok_rx.module.photo.presenter;

import com.yang.retrofit_ok_rx.base.BasePresenterImpl;
import com.yang.retrofit_ok_rx.bean.PhotoModel;
import com.yang.retrofit_ok_rx.http.HttpResponse;
import com.yang.retrofit_ok_rx.module.photo.model.IPhotoListInterceptor;
import com.yang.retrofit_ok_rx.module.photo.model.IPhotoListInterceptorImpl;
import com.yang.retrofit_ok_rx.module.photo.view.IPhotoListView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class IPhotoListPresenterImpl extends BasePresenterImpl<IPhotoListView,HttpResponse<PhotoModel>>
        implements IPhotoListPresenter {

    private final IPhotoListInterceptor<HttpResponse<PhotoModel>> interceptor;

    public IPhotoListPresenterImpl(IPhotoListView view) {
        super(view);
        interceptor = new IPhotoListInterceptorImpl();
    }

    @Override
    public void getPhotos(String id, int pageNum) {
        mSubscription = interceptor.getPhotoList(this, id, pageNum);
    }

    @Override
    public void requestSuccess(HttpResponse<PhotoModel> data) {
        List<PhotoModel.PictureBody> pictureBodyList = data.getShowapi_res_body().pagebean.contentlist;
        if (pictureBodyList != null && pictureBodyList.size() > 0) {
            getView().onLoadSuccess(pictureBodyList);
        }
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        getView().onLoadError(msg);
    }
}
