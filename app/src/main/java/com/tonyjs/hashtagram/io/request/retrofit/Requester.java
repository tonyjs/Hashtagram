package com.tonyjs.hashtagram.io.request.retrofit;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public interface Requester {
    String END_POINT = "https://api.instagram.com";

    @GET("/v1//users/self/feed")
    public void getNewsFeed(
            @Query("access_token") String accessToken, Callback<NewsFeedResponse> callback);

    @GET("/v1//users/self/feed")
    public NewsFeedResponse getNewsFeed(@Query("access_token") String accessToken);

    @POST("/oauth/access_token")
    public HostInfoResponse getHostInfo(@Query("code") String code,
                                        @Query("access_token") String accessToken,
                                        Callback<HostInfoResponse> callback);
}
