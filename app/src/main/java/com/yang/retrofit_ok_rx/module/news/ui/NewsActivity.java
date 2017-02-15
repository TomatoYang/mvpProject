package com.yang.retrofit_ok_rx.module.news.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.application.ActivityManager;
import com.yang.retrofit_ok_rx.base.BaseActivity;
import com.yang.retrofit_ok_rx.base.BaseFragment;
import com.yang.retrofit_ok_rx.base.BaseFragmentAdapter;
import com.yang.retrofit_ok_rx.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

@ParamsInfo(contentViewId = R.layout.activity_base, toolBarTitle = R.string.news,
        itemCheck = R.id.navi_news)
public class NewsActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_page)
    ViewPager viewPage;

    @Override
    protected void initView(Bundle savedInstanceState) {

        ActivityManager.getInstance().navigationActivity(getClass().getName(), false);

        List<BaseFragment> fragmentList = new ArrayList<>();
        List<String> titles = Arrays.asList("游戏", "科技", "体育");
        fragmentList.add(NewsListFragment.getInstance("T1348654151579"));
        fragmentList.add(NewsListFragment.getInstance("T1348649580692"));
        fragmentList.add(NewsListFragment.getInstance("T1348649079062"));

        BaseFragmentAdapter adapter = new BaseFragmentAdapter
                (getSupportFragmentManager(), fragmentList, titles);
        viewPage.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPage);
        ViewUtil.dynamicSetTabLayoutMode(tabLayout);
    }

}
