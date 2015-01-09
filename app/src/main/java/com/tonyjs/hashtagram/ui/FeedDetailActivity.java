package com.tonyjs.hashtagram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.model.Feed;
import com.tonyjs.hashtagram.ui.widget.SlipLayoutController;
import com.tonyjs.hashtagram.ui.widget.SlipScrollView;

/**
 * Created by orcpark on 14. 11. 13..
 */
public class FeedDetailActivity extends BaseActivity
                implements FeedDetailFragment.SlipControlCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initLayout();
    }

    private void initLayout() {
        Toolbar toolbar = getToolBar();

        Intent intent = getIntent();

        String title = intent != null ? intent.getStringExtra("hashtag") : null;
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitle("#" + title);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_up);

        Feed item = getItem();
        addFragment(FeedDetailFragment.newInstance(item));
    }

    private Feed getItem() {
        return getIntent() != null ? (Feed) getIntent().getSerializableExtra("item") : null;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("item", getItem());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContainerResId() {
        return R.id.fragment_container;
    }

    @Override
    public void onControllerCreated(SlipScrollView slipScrollView) {
        getSlipController().setDirection(SlipLayoutController.DIRECTION_TO_UP);
        getSlipController().setTargetView(findViewById(R.id.toolbar));
        getSlipController().setScrollView(slipScrollView);
    }

//    @Override
//    public void onSlipLayoutCreated(SlipLayout slipLayout) {
//        slipLayout.setTargetView(mToolBar);
//    }
}
