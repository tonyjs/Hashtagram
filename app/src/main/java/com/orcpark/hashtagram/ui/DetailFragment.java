package com.orcpark.hashtagram.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.InstaImageInfo;
import com.orcpark.hashtagram.io.model.insta.InstaImageSpec;
import com.orcpark.hashtagram.io.model.insta.InstaItem;
import com.orcpark.hashtagram.ui.widget.DragLayout;
import com.orcpark.hashtagram.util.ImageLoader;

/**
 * Created by orcpark on 14. 11. 12..
 */
public class DetailFragment extends BaseFragment {
    public static final String KEY_ITEM = "item";
    public static DetailFragment newInstance(InstaItem item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.iv_thumb)
    ImageView mIvThumb;
    @InjectView(R.id.tv_summary)
    TextView mTvSummary;
    @InjectView(R.id.drag_layout)
    DragLayout mDragLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InstaItem item = getItem();
        if (item == null) {
            return;
        }

        InstaImageInfo info = item.getImageInfo();
        InstaImageSpec spec = info != null ? info.getStandard() : null;
        final String thumbUrl = spec != null ? spec.getUrl() : null;
        ImageLoader.load(mActivity, thumbUrl, mIvThumb);

        String summary = item.getCaption() != null ? item.getCaption().getTitle() : null;
        mTvSummary.setText(summary);
    }

    private InstaItem getItem() {
        Bundle args = getArguments();
        return (InstaItem) args.getSerializable(KEY_ITEM);
    }

}
