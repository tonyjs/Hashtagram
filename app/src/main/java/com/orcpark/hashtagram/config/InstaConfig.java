package com.orcpark.hashtagram.config;

/**
 * Created by junseon on 2014-03-31.
 */
public interface InstaConfig {
    String INSTA_CLIENT_ID = "28019466f9ec4299840c765a7b3335f2";

    String INSTA_CLIENT_SECRET = "d54f30ccb8324fabbbe1deabacd9c039";

    String INSTA_REDIRECT_URI = "http://hashtagram.com";

    String INSTA_AUTHORIZATION_URL =
            "https://instagram.com/oauth/authorize/?"
                    + "client_id=" + INSTA_CLIENT_ID
                    + "&redirect_uri=" + INSTA_REDIRECT_URI
//                    + "&response_type=token"
                    + "&response_type=code"
                    + "&scope=likes+comments";
}
