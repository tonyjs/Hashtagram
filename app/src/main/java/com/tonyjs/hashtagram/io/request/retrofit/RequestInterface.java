package com.tonyjs.hashtagram.io.request.retrofit;

import com.tonyjs.hashtagram.io.model.HostInfo;
import com.tonyjs.hashtagram.io.response.NewsFeedResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.*;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public interface RequestInterface {
    String END_POINT = "https://api.instagram.com";

    @GET("/v1//users/self/feed")
    public void getNewsFeed(
            @Query("access_token") String accessToken, Callback<NewsFeedResponse> callback);

    @GET("/v1//users/self/feed")
    public NewsFeedResponse getNewsFeed(@Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    public HostInfo getHostInfo(@FieldMap Map<String, String> fieldMap);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    public void getHostInfo(@FieldMap Map<String, String> fieldMap,
                            Callback<HostInfo> callback);
}
