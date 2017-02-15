package com.yang.retrofit_ok_rx.http;

import com.yang.retrofit_ok_rx.bean.PhotoModel;

import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/4.
 */
public interface PhotoService {

    @POST(Api.PICTURES_URL)
    @Headers("apikey: " + Api.API_KEY)
    Observable<HttpResponse<PhotoModel>> getPhotos(
            @Header("Cache-Control") String cacheControl,
            @Query("type") String type,
            @Query("page") int page);

}
