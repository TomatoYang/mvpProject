package com.yang.retrofit_ok_rx.base;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class BaseBean {

    private int code;

    private String message;

    private String data;

    public BaseBean() {
    }

    public boolean isSuccess() {
        return getCode() == 1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    /**
     * 解析JsonObject，你的{@link T}必须提供默认无参构造
     *
     * @param clazz 要解析的实体类的class
     * @param <T> 实体类泛型
     * @return 实体类
     */
    public <T> T parseObj(Class<T> clazz) {
        T t = null;
        try {
            String str = null;
            if (getData().startsWith("[")) {
                str = getData().replace("[", "").replace("]", "");
            } else {
                str = getData();
            }
            t = JSON.parseObject(str, clazz);
        } catch (Exception e1) {
            // 服务端数据格式错误时，返回data的空构造
            Log.e("BaseModel parseObj", e1.toString());
            try {
                t = clazz.newInstance();
            } catch (Exception e2) {
            }
        }
        return t;
    }

    /**
     * 解析JsonArray，你的{@link T}必须提供默认无参构造
     * @param clazz 要解析的实体类的class
     * @param <T> 实体类泛型
     * @return 实体类的List集合
     */
    public <T> List<T> parseArray(Class<T> clazz) {
        List<T> e = new ArrayList<>();
        try {
            e = JSON.parseArray(getData(), clazz);
        } catch (Exception e1) {
        }
        return e;
    }
}
