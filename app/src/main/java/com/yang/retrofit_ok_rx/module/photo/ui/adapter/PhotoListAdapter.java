package com.yang.retrofit_ok_rx.module.photo.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.base.BaseRecyclerViewAdapter;
import com.yang.retrofit_ok_rx.base.BaseRecyclerViewHolder;
import com.yang.retrofit_ok_rx.bean.PhotoModel;
import com.yang.retrofit_ok_rx.http.ImageLoader;
import com.yang.retrofit_ok_rx.widget.PhotoView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public class PhotoListAdapter extends BaseRecyclerViewAdapter<PhotoModel.PictureBody> {

    public PhotoListAdapter(Context context, List<PhotoModel.PictureBody> list) {
        super(context, list);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.item_photo_list;
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position) {
        PhotoModel.PictureBody pictureBody = list.get(position);
        String middle = pictureBody.list.get(0).middle;
        if (!TextUtils.isEmpty(middle)) {
//            Log.e("photoUrl", middle);
            /*Glide.with(context).load(middle)
                    .centerCrop()
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_fail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) holder.getViewById(R.id.item_photo_iv));*/
            ImageLoader.load(context, middle, (PhotoView) holder.getViewById(R.id.item_photo_iv));
        }
    }
}
