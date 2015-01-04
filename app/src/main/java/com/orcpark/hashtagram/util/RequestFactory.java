package com.orcpark.hashtagram.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.volley.Request;
import com.orcpark.hashtagram.config.InstaConfig;
import com.orcpark.hashtagram.io.request.JsonObjectRequester;
import com.orcpark.hashtagram.io.request.RequestQueueManager;
import com.orcpark.hashtagram.io.request.Requester;
import com.orcpark.hashtagram.io.request.ResponseListener;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tony.park on 14. 11. 4..
 */
public class RequestFactory {

    public static void getNewsfeed(Context context, View progressBar, ResponseListener listener) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = "https://api.instagram.com/v1/users/self/feed"
                        + getEncodedParams(ParamFactory.getAccessTokenParams(accessToken));
        request(context, Request.Method.GET, url, null, progressBar, listener);
    }

    public static void getNextItems(Context context, String url, View progressBar, ResponseListener listener) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        request(context, Request.Method.GET, url, null, progressBar, listener);
    }

    public static void getHashTag(Context context, String hashTag,
                                        View progressBar, ResponseListener listener) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = "https://api.instagram.com/v1/tags/"+ getEncodeString(hashTag) +
                "/media/recent" + getEncodedParams(ParamFactory.getAccessTokenParams(accessToken));

        request(context, Request.Method.GET, url, null, progressBar, listener);
    }

    public static void getUser(Context context, String code,
                               Dialog dialog, ResponseListener listener) {
        if(TextUtils.isEmpty(code)){
            return;
        }
        String url = "https://api.instagram.com/oauth/access_token";

        HashMap<String, String> params = ParamFactory.getUserParams(code);

        request(context, Request.Method.POST, url, params, dialog, listener);

    }

    public static void postComment(
            Context context, String mediaId, String comment, View progressBar, ResponseListener listener) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = "https://api.instagram.com/v1/media/"+ mediaId + "/comments";

        HashMap<String, String> params = ParamFactory.getPostCommentParams(accessToken, comment);

        request(context, Request.Method.POST, url, params, progressBar, listener);
    }

    public static void postLike(Context context, String mediaId,
                                View progressBar, ResponseListener listener){
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = "https://api.instagram.com/v1/media/"+ mediaId + "/likes";

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        request(context, Request.Method.POST, url, params, progressBar, listener);
    }

    public static void postUnLike(Context context, String mediaId,
                                View progressBar, ResponseListener listener){
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = "https://api.instagram.com/v1/media/"+ mediaId + "/likes";

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        request(context, Request.Method.DELETE, url, params, progressBar, listener);
    }

    protected static void request(Context context, int method, String url, HashMap<String, String> params,
                                  View progressBar, ResponseListener listener) {

        boolean useUrlParameter =
                (method == Request.Method.GET) || (method == Request.Method.DELETE);
        if (useUrlParameter) {
            url += getEncodedParams(params);
        }

        JsonObjectRequester requester = new JsonObjectRequester(method, url, listener);
        if (!useUrlParameter && params != null && params.size() >= 0) {
            requester.setParams(params);
        }

        requester.setProgressView(progressBar);

        RequestQueueManager.getInstance(context).getRequestQueue().add(requester);
    }

    protected static void request(Context context, int method, String url, HashMap<String, String> params,
                                  Dialog dialog, ResponseListener listener) {

        boolean useUrlParameter =
                (method == Request.Method.GET) || (method == Request.Method.DELETE);
        if (useUrlParameter) {
            url += getEncodedParams(params);
        }

        JsonObjectRequester requester = new JsonObjectRequester(method, url, listener);
        if (!useUrlParameter && params != null && params.size() >= 0) {
            requester.setParams(params);
        }

        requester.setProgressDialog(dialog);

        RequestQueueManager.getInstance(context).getRequestQueue().add(requester);
    }

    /**
     * Get 방식 연동시 BasicNameValuePair 를 이용해 만들어진 HashMap 을
     * Url 파라미터로 만들어 줌(ex. ?access_token=123456&device=Galaxy)
     *
     * @param params BasicNameValuePair 를 이용해서 만들어진 Map
     * @return String
     */
    public static String getEncodedParams(HashMap<String, String> params) {
        if (params == null || params.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            int max = entries.size();
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), Requester.CHARSET));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), Requester.CHARSET));
                if (i != max - 1) {
                    sb.append("?");
                }
                i++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = TextUtils.isEmpty(sb.toString()) ? "" : "?" + sb.toString();
        return result;
    }

    public static String getEncodeString(String string) {
        String encodeString = null;

        try {
            encodeString = URLEncoder.encode(string, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodeString;
    }

    /**
     * Parameter 만들어주는 class
     */
    public static final class ParamFactory {

        public static HashMap<String, String> getAccessTokenParams(String accessToken) {
            return getParams(
                    new BasicNameValuePair("access_token", accessToken));
        }

        public static HashMap<String, String> getUserParams(String code) {
            return getParams(
                    new BasicNameValuePair("code", code),
                    new BasicNameValuePair("client_id", InstaConfig.INSTA_CLIENT_ID),
                    new BasicNameValuePair("client_secret", InstaConfig.INSTA_CLIENT_SECRET),
                    new BasicNameValuePair("grant_type", "authorization_code"),
                    new BasicNameValuePair("redirect_uri", InstaConfig.INSTA_REDIRECT_URI)
                    );
        }

        public static HashMap<String, String> getPostCommentParams(String accessToken, String comment){
            return getParams(
                    new BasicNameValuePair("access_token", accessToken),
                    new BasicNameValuePair("text", comment)
                    );
        }

        public static HashMap<String, String> getParams(BasicNameValuePair... pairs) {
            HashMap<String, String> params = new HashMap<String, String>();
            for (BasicNameValuePair pair : pairs) {
                params.put(pair.getName(), pair.getValue());
            }
            return params;
        }
    }

}
