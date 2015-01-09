package com.tonyjs.hashtagram.io.request.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;

import org.apache.http.protocol.HTTP;
import java.io.UnsupportedEncodingException;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class GsonRequest extends BasicRequest {

    private Class mClass;
    public GsonRequest(int method, String url, Class target, Callback callback) {
        super(method, url, callback);
        mClass = target;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String gsonString = null;
        Gson gson = new Gson();
        try {
            gsonString = new String(response.data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            deliverError(new ParseError(e));
            return null;
        } catch (Exception e) {
            deliverError(new VolleyError(e));
            return null;
        }
        return Response.success(gson.fromJson(gsonString, mClass),
                HttpHeaderParser.parseCacheHeaders(response));
    }

}
