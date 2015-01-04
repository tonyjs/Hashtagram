package com.tonyjs.hashtagram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.db.HashtagramDatabase;
import com.tonyjs.hashtagram.ui.adapter.EditHashtagAdapter;

import java.util.ArrayList;

/**
 * Created by orcpark on 14. 11. 16..
 */
public class EditHashtagFragment extends BaseFragment {
    @InjectView(R.id.list_view) ListView mListView;
    @OnClick(R.id.btn_apply) void apply(){
        ArrayList<EditHashtagAdapter.CheckHashtag> items = mAdapter.getItems();
        if (items == null || items.size() <= 0) {
            return;
        }

        HashtagramDatabase database = HashtagramDatabase.getInstance(mActivity);
        database.deleteAll();
        database.insert(MainActivity.NEWSFEED);

        for (EditHashtagAdapter.CheckHashtag hashtag : items) {
            if (hashtag.isChecked()) {
                database.insert(hashtag.getHashtag());
            }
        }

        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_hashtag, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    private EditHashtagAdapter mAdapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new EditHashtagAdapter(mActivity);
        ArrayList<EditHashtagAdapter.CheckHashtag> items =
                mAdapter.getItems(HashtagramDatabase.getInstance(mActivity).getAllHashTag());
        mAdapter.setItems(items);
        mListView.setAdapter(mAdapter);
    }
}
