package com.tonyjs.hashtagram.io.request.retrofit;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tonyjs on 15. 1. 7..
 */
public interface Requester {
    String END_POINT = "https://api.instagram.com/v1/";

    @GET("/users/self/feed")
    public void getNewsFeed(
            @Query("access_token") String accessToken, Callback<NewsFeedResponse> callback);

}
