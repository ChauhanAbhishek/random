package com.rapido.youtube_rapido.app.service;

import com.rapido.youtube_rapido.model.response.VideoResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adityachauhan on 23/06/18.
 *
 */


public interface ApiService {

    @GET("/youtube/v3/videos")
    Single<Response<VideoResponse>> getVideos(@Query("part") String part,
                                              @Query("chart") String chart,
                                              @Query("regionCode") String regionCode,
                                              @Query("key") String key,
                                              @Query("maxResults") Integer maxResults,
                                              @Query("pageToken") String pageToken);

    @GET("/youtube/v3/videos")
    Single<Response<VideoResponse>> getVideos(@Query("part") String part,
                                                           @Query("chart") String chart,
                                                           @Query("regionCode") String regionCode,
                                                           @Query("key") String key,
                                                           @Query("maxResults") Integer maxResults);

}
