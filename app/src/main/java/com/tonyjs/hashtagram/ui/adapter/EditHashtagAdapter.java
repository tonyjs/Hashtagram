package com.tonyjs.hashtagram.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.ui.adapter.base.BasicAdapter;
import com.tonyjs.hashtagram.ui.adapter.base.SparseViewHolder;

import java.util.ArrayList;

/**
 * Created by orcpark on 14. 11. 16..
 */
public class EditHashtagAdapter extends BasicAdapter<EditHashtagAdapter.CheckHashtag> {

    public EditHashtagAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_edit_hashtag, parent, false);
        }

        final CheckHashtag item = getItem(position);
        final CheckBox cbCheck = SparseViewHolder.get(convertView, R.id.cb_check);
        TextView tvTitle = SparseViewHolder.get(convertView, R.id.tv_title);
        tvTitle.setText(item.getHashtag());
        cbCheck.setChecked(item.checked);
        cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(cbCheck.isChecked());
                cbCheck.setChecked(item.isChecked());
            }
        });
        return convertView;
    }

    public ArrayList<CheckHashtag> getItems(ArrayList<String> items) {
        ArrayList<CheckHashtag> newItems = new ArrayList<>();
        for (String s : items) {
            newItems.add(new CheckHashtag(s));
        }
        newItems.remove(0);
        return newItems;
    }

    public static class CheckHashtag {
        private boolean checked = true;
        private String hashtag;

        public CheckHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }
    }
}
