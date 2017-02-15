package com.yang.retrofit_ok_rx.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {


    private List<BaseFragment> fragmentList;
    private List<String> titleList;

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String>
            titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
