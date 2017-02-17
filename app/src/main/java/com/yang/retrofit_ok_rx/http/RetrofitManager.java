package com.yang.retrofit_ok_rx.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yang.retrofit_ok_rx.application.App;
import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;
import com.yang.retrofit_ok_rx.bean.PhotoModel;
import com.yang.retrofit_ok_rx.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/4.
 */
public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    //设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
    private static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private static volatile RetrofitManager instance;
    private static volatile OkHttpClient okHttpClient;

    //配置OkHTTP的云端响应头拦截，配置缓存策略
    private Interceptor reWriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.e("请求接口", request.url().toString());
            if (!NetUtil.isConnected(App.getApplication())) {
                Log.e(TAG, "没有网络连接");
                request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if (NetUtil.isConnected(App.getApplication())) {
                String cacheControl = response.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    // 打印返回的json数据拦截器,这个其实加不加无所谓,单纯打印的话也可以在callback中进行
    private Interceptor loggingJsonInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            ResponseBody body = response.body();
            long contentLength = body.contentLength();

            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    Log.e(TAG, "Couldn't decode the response body");
                    return response;
                }
            }
            if (contentLength != 0) {
                Log.e(TAG, "开始打印JSON数据");
                Log.e(TAG, buffer.clone().readString(charset));
            }
            return response;
        }
    };



    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    //图片
    private final PhotoService photoService;
    private final NewsService newsService;

    private RetrofitManager() {
        photoService = buildService(Api.BAIDU_API, PhotoService.class);
        newsService = buildService(Api.NETEASY_API, NewsService.class);
    }

    public <T> T buildService(String baseUrl, Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (okHttpClient == null) {
                    Cache cache = new Cache(new File(App.getApplication().getCacheDir(),
                            "YHttpCache"), 1024 * 1024 * 50);
                    okHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(reWriteCacheControlInterceptor)
                            .addInterceptor(reWriteCacheControlInterceptor)
                            .addInterceptor(loggingJsonInterceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 根据网络状况获取缓存的策略
     *
     * @return
     */
    @NonNull
    private String getCacheControl() {
        return NetUtil.isConnected(App.getApplication())
                ? CACHE_CONTROL_NETWORK
                : CACHE_CONTROL_CACHE;
    }

    /**
     * 获取图片资源
     * @param id
     * @param page
     * @return
     */
    public Observable<HttpResponse<PhotoModel>> getPhotoListObservable(String id, int page) {
        return photoService.getPhotos(getCacheControl(), id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    /**
     * 获取新闻资源
     * @param id
     * @param page
     * @return
     */
    public Observable<Map<String, List<NeteastNewsSummary>>> getNewsListObservable(String id, int page) {
        return newsService.getNewsList(getCacheControl(), "list", id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }







}
