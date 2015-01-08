package com.tonyjs.hashtagram.io.request.retrofit;

import com.google.gson.annotations.SerializedName;
import com.tonyjs.hashtagram.config.InstaConfig;

/**
 * Created by tony.park on 15. 1. 8..
 */
public class HostInfoBody {
    @SerializedName("code") private String code;
    @SerializedName("client_id") private String clientId;
    @SerializedName("client_secret") private String clientSecret;
    @SerializedName("grant_type") private String grantType;
    @SerializedName("redirect_uri") private String redirectUri;

    public HostInfoBody(String code) {
        this.code = code;
        clientId = InstaConfig.INSTA_CLIENT_ID;
        clientSecret = InstaConfig.INSTA_CLIENT_SECRET;
        grantType = "authorization_code";
        redirectUri = InstaConfig.INSTA_REDIRECT_URI;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
