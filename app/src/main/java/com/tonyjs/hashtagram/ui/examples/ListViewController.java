package com.tonyjs.hashtagram.ui.examples;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.tonyjs.hashtagram.io.model.insta.InstaItem;
import com.tonyjs.hashtagram.io.request.volley.ResponseListener;
import com.tonyjs.hashtagram.ui.adapter.BasicAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tony.park on 14. 11. 4..
 *
 * List 형식의 Data 를 Response 로 받고
 * Data 를 가공하여 Adapter 에 넣어주는 형식의 class
 */
public class ListViewController implements ResponseListener<JSONObject> {

    private Context mContext;
    private BasicAdapter<InstaItem> mAdapter;
    public ListViewController(Context context, BasicAdapter<InstaItem> adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    /*
        Request method 호출
     */
    public void run() {
        if (!validateContext()) {
            return;
        }
//        RequestFactory.getItems(mContext, this);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (!validateContext()) {
            return;
        }

        if (response == null) {
            Toast.makeText(mContext, "response is null", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray arr = response.optJSONArray("data");
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Model>>(){}.getType();
//
//        ArrayList<Model> items = gson.fromJson(arr.toString(), type);
//        Toast.makeText(mContext, items.toString(), Toast.LENGTH_LONG).show();
//        mAdapter.setItems(items);
    }

    @Override
    public void onError(VolleyError error) {
        if (!validateContext()) {
            return;
        }
        Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean validateContext() {
        return mContext != null;
    }

}
