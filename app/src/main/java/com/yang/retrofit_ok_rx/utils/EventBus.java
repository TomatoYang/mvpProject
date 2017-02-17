package com.yang.retrofit_ok_rx.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/2/9.
 */
public class EventBus {

    private static volatile EventBus instance;

    private ConcurrentMap<Object,List<Subject>> map;

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    private EventBus() {
        map = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param tag K值,用于标志Observable
     * @param clazz 用于确认需要操作的类型,发送的对象
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(Object tag, Class<T> clazz) {
        List<Subject> list = map.get(tag);
        if (list == null) {
            list = new ArrayList<>();
        }
        //第一个T决定类Observable的泛型,第二个决定接口Observer中onNext的泛型
        Subject<T, T> subject = PublishSubject.create();
        list.add(subject);
        map.put(tag, list);
        return subject;
    }

    /**
     *
     * @param tag K值,用于标志Observable
     * @param observable 需要移除的Observable
     */
    public void unRegister(Object tag, @NonNull Observable observable) {
        List<Subject> list = map.get(tag);
        if (list == null) {
            return;
        }
        list.remove(observable);
        if (list.size() == 0) {
            map.remove(tag);
        }
    }

    @SuppressWarnings("unchecked")
    public void post(Object tag, Object content) {
        List<Subject> list = map.get(tag);
        if (list != null && list.size()!=0) {
            for (Subject subject : list) {
                subject.onNext(content);
            }
        }
    }



}
