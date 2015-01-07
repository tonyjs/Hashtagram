package com.tonyjs.hashtagram.io.request.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by tony.park on 14. 11. 4..
 *
 * Response 가 JSONArray 일 때 쓰는 class
 */
public class JsonArrayRequester extends Requester<JSONArray> {

    public JsonArrayRequester(int method, String url, ResponseListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        String stringData = null;
        JSONArray result = null;
        try {
            stringData = new String(response.data, CHARSET);
            result = new JSONArray(stringData);
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
    public int compareTo(Request<JSONArray> another) {
        return super.compareTo(another);
    }
}
