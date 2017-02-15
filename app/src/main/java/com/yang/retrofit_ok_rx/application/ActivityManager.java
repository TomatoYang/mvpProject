package com.yang.retrofit_ok_rx.application;

import android.util.Log;
import android.util.SparseArray;

/**
 * Created by Administrator on 2017/2/4.
 */
public class ActivityManager {

    private static final String TAG = "ActivityManager";
    private static volatile ActivityManager instance;

    /**
     * 模拟管理启动activity栈的情况,这里通过aty的className进行管理
     * SparseArray在特定情况下性能比HasMap要好
     */
    private SparseArray<String> activityArray;

    private ActivityManager() {
        activityArray = new SparseArray<>();
    }

    public SparseArray<String> getActivityArray() {
        return activityArray;
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取上一个导航Activity的类
     * @return 上一个导航Activity的类
     */
    public Class getLastNavActivity() throws ClassNotFoundException {
        if (activityArray == null || activityArray.size() == 0 || activityArray
                .size() == 1) {
            return null;
        }
        return Class.forName(activityArray.get(activityArray.size() - 2));
    }

    /**
     * 获得当前显示的Activity的类
     * @return 当前显示的Activity的类
     * @throws ClassNotFoundException
     */
    public Class getCurrentNavActivity() throws ClassNotFoundException {
        if (activityArray == null || activityArray.size() == 0) {
            return null;
        }
        return Class.forName(activityArray.get(activityArray.size() - 1));
    }

    public void navigationActivity(String className, boolean isBack) {
        if (activityArray == null) {
            activityArray = new SparseArray<>();
        }

        if (isBack) {
            //这里是后退
            //需要把最前面的aty的类名放到最后0的位置上,务必需要在打乱顺序前记录最后的类名
            int index = activityArray.indexOfValue(className);
            className = activityArray.valueAt(activityArray.size() - 1);
            for (int i = index; i >= 0; i--) {
                activityArray.put(i + 1, activityArray.valueAt(i));
            }
            activityArray.put(0, className);

        } else {
            //这里是前进跳转
            //拿到需要跳转的界面在array中的下标位置
            int index = activityArray.indexOfValue(className);
            if (index == -1) {
                //array中未曾添加该界面
                activityArray.put(activityArray.size(), className);
            } else {
                //array添加过该界面,这时需要把该界面放到最前面的位置
                for (int i = index + 1; i < activityArray.size(); i++) {
                    activityArray.put(i - 1, activityArray.valueAt(i));
                }
                activityArray.put(activityArray.size() - 1, className);
            }

        }
        printOrder();
    }

    private void printOrder() {
        Log.e(TAG, "打印: " + activityArray.size());
        for (int i = 0; i < activityArray.size(); i++) {
            Log.e(TAG, "打印：" + activityArray.get(i));
        }
        Log.e(TAG, "打印结束");
    }
}
