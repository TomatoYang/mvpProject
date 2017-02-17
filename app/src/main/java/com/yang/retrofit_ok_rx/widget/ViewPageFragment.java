package com.yang.retrofit_ok_rx.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.base.BaseFragment;
import com.yang.retrofit_ok_rx.http.ImageLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/2/16.
 */
@ParamsInfo(contentViewId = R.layout.fragment_viewpager)
public class ViewPageFragment extends BaseFragment {

    @Bind(R.id.mask)
    View mask;
    @Bind(R.id.viewpager)
    ReboundViewPager viewpager;
    @Bind(R.id.tv_position)
    TextView tvPosition;

    private List<String> urlImagesList;
    private List<ImageInfo> imageInfoList;
    private ImageInfo imageInfo;
    private int index;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitFragment(v);
        }
    };

    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                exitFragment(v);
            }
            return false;
        }
    };

    public static BaseFragment getInstance(Bundle bundle) {
        BaseFragment fragment = new ViewPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onArgumentsReceive(Bundle bundle) {
        urlImagesList = bundle.getStringArrayList("urls");
        imageInfoList = bundle.getParcelableArrayList("imageInfos");
        imageInfo = bundle.getParcelable("imageInfo");
        index = bundle.getInt("index", 0);
    }

    @Override
    protected void initView(View view) {

        //执行进入动画,背景逐渐变黑
        enterAnimation();

        //初始化下标提示
        tvPosition.setText((index + 1) + "/" + urlImagesList.size());

        //初始化viewPage相关
        initViewPage();

    }

    private void initViewPage() {
        viewpager.getOverscrollView().setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urlImagesList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout
                                .layout_photoview_info,
                        container, false);
                final PhotoView photoView = (PhotoView) view.findViewById(R.id.image_detail);
                final MaterialProgressBar progressBar = (MaterialProgressBar) view.findViewById(R.id
                        .progress);
                if (position == index ) {
                    photoView.animateFrom(imageInfo);
                }

                ImageLoader.load(getActivity(), urlImagesList.get(position), new ImageLoader
                        .Callback() {

                    @Override
                    public void onLoadComplete(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        //图片加载完成关闭progressBar
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageDrawable(resource);
                    }
                });

                //设置photoView的相关设置
                photoView.setFocusableInTouchMode(true);
                photoView.requestFocus();
                photoView.setTag(position);
                photoView.touchEnable(true);

                photoView.setOnKeyListener(keyListener);
                photoView.setOnClickListener(clickListener);

                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewpager.getOverscrollView().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPosition.setText((position + 1) + "/" + urlImagesList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.getOverscrollView().setCurrentItem(index);
    }

    private void enterAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateInterpolator());
        mask.startAnimation(animation);
    }

    private void exitAnimation(final View view) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mask.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                popFragment();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mask.startAnimation(animation);
        view.startAnimation(animation);
    }

    private void exitFragment(View v) {
        //退出时点击的位置
        int position = (int) v.getTag();
        //回到上个界面该view的位置
        if(((FrameLayout)v.getParent()).getChildAt(1).getVisibility() == View.VISIBLE){
            popFragment();
        }else {
            exitAnimation(v);
            /*((PhotoView) v).animateTo(imageInfos.get(position), new Runnable() {
                @Override
                public void run() {
                    popFragment();
                }
            });*/
        }
    }

    private void popFragment() {
        if (!ViewPageFragment.this.isResumed()) {
            return;
        }
        FragmentManager manager = getFragmentManager();
        if (manager != null) {
            Log.e("popBackStack", "popBackStack");
            manager.popBackStack();
        }
    }



}
