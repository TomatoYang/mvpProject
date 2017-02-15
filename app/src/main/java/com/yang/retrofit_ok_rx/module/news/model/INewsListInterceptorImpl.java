package com.yang.retrofit_ok_rx.module.news.model;

import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;
import com.yang.retrofit_ok_rx.http.HttpCallback;
import com.yang.retrofit_ok_rx.http.RetrofitManager;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Administrator on 2017/2/8.
 */
public class INewsListInterceptorImpl implements INewsListInterceptor<List<NeteastNewsSummary>> {

    @Override
    public Subscription getNewsList(final HttpCallback<List<NeteastNewsSummary>> callback,
                                    final String id, int startPage) {
        return RetrofitManager.getInstance()
                .getNewsListObservable(id, startPage)
                .flatMap(new Func1<Map<String, List<NeteastNewsSummary>>,
                        Observable<NeteastNewsSummary>>() {
                    @Override
                    public Observable<NeteastNewsSummary> call(
                            Map<String, List<NeteastNewsSummary>> stringListMap) {
                        return Observable.from(stringListMap.get(id));
                    }
                })
                .toSortedList(new Func2<NeteastNewsSummary, NeteastNewsSummary, Integer>() {
                    // 按时间先后排序
                    @Override
                    public Integer call(NeteastNewsSummary neteastNewsSummary, NeteastNewsSummary
                            neteastNewsSummary2) {
                        return neteastNewsSummary2.ptime.compareTo(neteastNewsSummary.ptime);
                    }
                })
                .subscribe(new Subscriber<List<NeteastNewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(e.toString());
                    }

                    @Override
                    public void onNext(List<NeteastNewsSummary> list) {
                        callback.requestSuccess(list);
                    }
                });
    }
}
