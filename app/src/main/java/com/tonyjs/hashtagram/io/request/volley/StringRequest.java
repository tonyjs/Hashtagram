package com.tonyjs.hashtagram.io.request.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.tonyjs.hashtagram.io.request.volley.response.Callback;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

/**
 * Created by tonyjs on 15. 1. 9..
 */
public class StringRequest extends BasicRequest<String> {

    public StringRequest(int method, String url, Callback callback) {
        super(method, url, callback);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String result = null;
        try {
            result = new String(response.data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            deliverError(new ParseError(e));
            return null;
        } catch (Exception e) {
            deliverError(new VolleyError(e));
            return null;
        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

}
