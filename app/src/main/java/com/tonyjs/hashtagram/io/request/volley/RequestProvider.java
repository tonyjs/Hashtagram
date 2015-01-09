package com.tonyjs.hashtagram.io.request.volley;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.volley.Request;
import com.tonyjs.hashtagram.config.InstaConfig;
import com.tonyjs.hashtagram.io.model.HostInfo;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;
import com.tonyjs.hashtagram.io.response.NewsFeedResponse;
import com.tonyjs.hashtagram.util.PrefUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class RequestProvider {
    public interface ApiInterface {
        String END_POINT = "https://api.instagram.com";

        String NEWS_FEED = "/v1/users/self/feed";

        String HASH_TAG = "/v1/tags/%s/media/recent";

        String HOST_INFO = "/oauth/access_token";

        String LIKES = "/v1/media/%s/likes";
    }

    public static void getNewsFeed(Context context, View progressBar, Callback callback) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = ApiInterface.END_POINT + ApiInterface.NEWS_FEED;

        int method = Request.Method.GET;

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        BasicRequest request = getGsonRequest(method, url, NewsFeedResponse.class,
                                                params, progressBar, null, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static void getNewsFeed(Context context, String nextUrl,
                                   View progressBar, Callback callback) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        int method = Request.Method.GET;

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        BasicRequest request = getGsonRequest(method, nextUrl, NewsFeedResponse.class,
                                                params, progressBar, null, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static void getHashTaggedFeed(Context context, String hashTag,
                                   View progressBar, Callback callback) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = String.format(ApiInterface.HASH_TAG, getEncodeString(hashTag));

        int method = Request.Method.GET;

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        BasicRequest request = getGsonRequest(method, url, NewsFeedResponse.class,
                                                params, progressBar, null, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static void getHostInfo(Context context, String code,
                                   Dialog dialog, Callback callback) {
        String url = ApiInterface.END_POINT + ApiInterface.HOST_INFO;

        int method = Request.Method.POST;

        HashMap<String, String> params = ParamFactory.getHostInfoParams(code);

        BasicRequest request = getGsonRequest(method, url,HostInfo.class,
                                                params, null, dialog, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static void postLikes(Context context, String mediaId,
                                   View progressBar, Callback callback) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = String.format(ApiInterface.LIKES, mediaId);

        int method = Request.Method.POST;

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        BasicRequest request = getStringRequest(method, url, params, progressBar, null, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static void postUnLikes(Context context, String mediaId,
                                   View progressBar, Callback callback) {
        String accessToken = PrefUtils.getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            Log.e("jsp", "has not access_token");
            return;
        }

        String url = String.format(ApiInterface.LIKES, mediaId);

        int method = Request.Method.DELETE;

        HashMap<String, String> params = ParamFactory.getAccessTokenParams(accessToken);

        BasicRequest request = getStringRequest(method, url, params, progressBar, null, callback);

        RequestManager.getInstance().getRequestQueue(context).add(request);
    }

    public static GsonRequest getGsonRequest(int method, String url,
                                             Class target, HashMap<String, String> params,
                                             View progress, Dialog dialog, Callback callback) {
        boolean urlParam = method == Request.Method.GET || method == Request.Method.DELETE;
        if (urlParam) {
            url += getEncodedParams(params);
        }

        GsonRequest request = new GsonRequest(method, url, target, callback);
        if (!urlParam) {
            request.setParams(params);
        }
        request.setProgressView(progress);
        request.setDialog(dialog);
        return request;
    }

    public static StringRequest getStringRequest(int method, String url,
                                                 HashMap<String, String> params,
                                                 View progress, Dialog dialog, Callback callback) {
        boolean urlParam = method == Request.Method.GET || method == Request.Method.DELETE;
        if (urlParam) {
            url += getEncodedParams(params);
        }

        StringRequest request = new StringRequest(method, url, callback);
        if (!urlParam) {
            request.setParams(params);
        }
        request.setProgressView(progress);
        request.setDialog(dialog);
        return request;
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
                sb.append(URLEncoder.encode(entry.getKey(), HTTP.UTF_8));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8));
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

        public static HashMap<String, String> getHostInfoParams(String code) {
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
