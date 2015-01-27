package com.tonyjs.hashtagram.config;

/**
 * Created by junseon on 2014-03-31.
 */
public interface InstaConfig {
    String INSTA_CLIENT_ID = "YOUR INSTAGRAM CLIENT ID";

    String INSTA_CLIENT_SECRET = "YOUR INSTAGRAM CLIENT SECRET";

    String INSTA_REDIRECT_URI = "http://hashtagram.com";

    String INSTA_AUTHORIZATION_URL =
            "https://instagram.com/oauth/authorize/?"
                    + "client_id=" + INSTA_CLIENT_ID
                    + "&redirect_uri=" + INSTA_REDIRECT_URI
//                    + "&response_type=token"
                    + "&response_type=code"
                    + "&scope=likes+comments";
}
