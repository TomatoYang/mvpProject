package com.yang.retrofit_ok_rx.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.annotation.ParamsInfo;
import com.yang.retrofit_ok_rx.application.ActivityManager;
import com.yang.retrofit_ok_rx.module.news.ui.NewsActivity;
import com.yang.retrofit_ok_rx.module.photo.ui.PhotoActivity;
import com.yang.retrofit_ok_rx.utils.Const;
import com.yang.retrofit_ok_rx.utils.EventBus;
import com.yang.retrofit_ok_rx.utils.GlideCircleTransform;
import com.yang.retrofit_ok_rx.utils.ViewUtil;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/2/4.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
        implements BaseView, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    /**
     * 布局的id
     */
    protected int mContentViewId;

    /**
     * Toolbar标题
     */
    private int mToolbarTitle;

    /**
     * navigation选中的itemID
     */
    private int checkItemID;

    /**
     * 监听每个界面,当完全退出时销毁每个界面
     */
    private Observable<Boolean> finishObservable;

    /**
     * 跳转的类,在抽屉菜单关闭时进行跳转
     */
    private Class mClass;

    /**
     * 抽屉菜单
     */
    private DrawerLayout drawer;
    private Toolbar toolbar;


    public T getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getClass().isAnnotationPresent(ParamsInfo.class)) {
            ParamsInfo annotation = getClass().getAnnotation(ParamsInfo.class);
            mContentViewId = annotation.contentViewId();
            mToolbarTitle = annotation.toolBarTitle();
            checkItemID = annotation.itemCheck();
        } else {
            throw new RuntimeException(
                    "Class must add annotations of ParamsInfo.class");
        }

        setContentView(mContentViewId);
        ButterKnife.bind(this);
        initView(savedInstanceState);

        initToolBar();
        setNavigationView();

        initFinishObservable();

    }

    private void initFinishObservable() {
        finishObservable = EventBus.getDefault().register(Const.EXIT_APP, Boolean.class);
        finishObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isFinish) {
                if (isFinish) {
                    finish();
                }
            }
        });
    }

    private void initToolBar() {

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                if (mToolbarTitle != -1) {
                    getSupportActionBar().setTitle(mToolbarTitle);
                }
            }
        }
    }

    private void setNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
                .open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mClass != null) {
                    showActivityToFront(BaseActivity.this, mClass, false);
                    mClass = null;
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            navigationView.post(new Runnable() {
                @Override
                public void run() {
                    ImageView iv = (ImageView) findViewById(R.id.avatar);
                    Glide.with(BaseActivity.this).load(R.drawable.header)
                            .transform(new GlideCircleTransform(BaseActivity.this))
                            .into(iv);
                }
            });
            if (checkItemID != -1) {
                Log.e("checkItemID", "" + checkItemID);
                navigationView.setCheckedItem(checkItemID);
            }
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    protected abstract void initView(Bundle savedInstanceState);

    public void showActivityToFront(Activity aty, Class<?> clazz, boolean isBack) {

        ActivityManager.getInstance().navigationActivity(clazz.getName(), isBack);

        Intent intent = new Intent(aty, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        aty.startActivity(intent);
        overridePendingTransition(0, 0);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            if (this instanceof NewsActivity) {
                Log.e("这是新闻界面", "回退退出");
                EventBus.getDefault().post(Const.EXIT_APP, true);
                return true;
            } else {
                try {
                    showActivityToFront(this,
                            ActivityManager.getInstance().getLastNavActivity(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "找不到类名啊");
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (finishObservable != null) {
            EventBus.getDefault().unRegister(Const.EXIT_APP, finishObservable);
        }

        ViewUtil.fixInputMethodManagerLeak(this);

    }

    protected Toast toast;

    @Override
    public void showToast(String msg) {
        if (toast != null) {
            toast.setText(msg);
        } else {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.isChecked()) return true;
        switch (item.getItemId()) {
            case R.id.navi_news:
                mClass = NewsActivity.class;
                break;
            case R.id.navi_photo:
                mClass = PhotoActivity.class;
                break;
            case R.id.navi_video:
                break;
            case R.id.navi_share:
                showToast("分享");
                break;
            case R.id.navi_send:
                showToast("发送");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
