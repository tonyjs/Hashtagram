package com.tonyjs.hashtagram.io.request.retrofit;

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

    @POST("/oauth/access_token")
    public void getHostInfo(@Field("code") String code,
                            @Field("client_id") String clientId,
                            @Field("client_secret") String clientSecret,
                            @Field("grant_type") String grantType,
                            @Field("redirect_uri") String redirectUri,
                            Callback<HostInfoResponse> callback);
    @POST("/oauth/access_token")
    public void getHostInfo(@Body HostInfoBody hostInfoBody,
                            Callback<HostInfoResponse> callback);
}
