package com.yang.retrofit_ok_rx.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yang.retrofit_ok_rx.R;

/**
 * Created by Administrator on 2017/2/16.
 */
public class ImageLoader {

    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void load(Context context, String url, final Callback callback) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                                                GlideDrawable> glideAnimation) {
                        callback.onLoadComplete(resource, glideAnimation);
                    }
                });
    }

    public interface Callback{
        void onLoadComplete(GlideDrawable resource, GlideAnimation<? super
                GlideDrawable> glideAnimation);
    }

}
