package com.orcpark.hashtagram.io.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by tony.park on 14. 11. 4..
 *
 * Response 가 JSONObject 일 때 쓰는 class
 */
public class JsonObjectRequester extends Requester<JSONObject> {

    public JsonObjectRequester(int method, String url, ResponseListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String stringData = null;
        JSONObject result = null;
        try {
            stringData = new String(response.data, CHARSET);
            result = new JSONObject(stringData);
        } catch (UnsupportedEncodingException e) {
            deliverError(new ParseError(e));
            return null;
        } catch (JSONException e) {
            deliverError(new ParseError(e));
            return null;
        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public int compareTo(Request<JSONObject> another) {
        return super.compareTo(another);
    }
}
