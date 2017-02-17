package com.yang.retrofit_ok_rx.module.photo.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.base.BaseActivity;
import com.yang.retrofit_ok_rx.base.BaseFragment;
import com.yang.retrofit_ok_rx.base.BaseFragmentAdapter;
import com.yang.retrofit_ok_rx.utils.EventBus;
import com.yang.retrofit_ok_rx.utils.ViewUtil;
import com.yang.retrofit_ok_rx.widget.ViewPageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;

@ParamsInfo(contentViewId = R.layout.activity_base, toolBarTitle = R.string.title_photo,
        itemCheck = R.id.navi_photo)
public class PhotoActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_page)
    ViewPager viewPage;
    private Observable<Bundle> bundleObservable;
    private ViewPageFragment viewPageFragment;

    @Override
    protected void initView(Bundle savedInstanceState) {

        List<BaseFragment> fragmentList = new ArrayList<>();
        List<String> titles = Arrays.asList("美女", "娱乐", "美食");
        fragmentList.add(PhotoListFragment.getInstance("4001"));
        fragmentList.add(PhotoListFragment.getInstance("6003"));
        fragmentList.add(PhotoListFragment.getInstance("3001"));

        BaseFragmentAdapter adapter = new BaseFragmentAdapter
                (getSupportFragmentManager(), fragmentList, titles);
        viewPage.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPage);
        ViewUtil.dynamicSetTabLayoutMode(tabLayout);

        initViewPageObservable();
    }


    private void initViewPageObservable() {
        bundleObservable = EventBus.getDefault().register("viewPageInit", Bundle.class);
        bundleObservable.subscribe(new Action1<Bundle>() {
            @Override
            public void call(Bundle bundle) {
                viewPageFragment = (ViewPageFragment) ViewPageFragment.getInstance(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(Window.ID_ANDROID_CONTENT,
                                viewPageFragment,
                                "tag")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unRegister("viewPageInit", bundleObservable);

    }

}
