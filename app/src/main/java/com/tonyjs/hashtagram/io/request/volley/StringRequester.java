package com.tonyjs.hashtagram.io.request.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by tony.park on 14. 11. 4..
 */
public class StringRequester extends Requester<String> {

    public StringRequester(int method, String url, ResponseListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String result = null;
        try {
            result = new String(response.data, CHARSET);
        } catch (UnsupportedEncodingException e) {
            result = new String(response.data);
        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public int compareTo(Request<String> another) {
        return super.compareTo(another);
    }
}
