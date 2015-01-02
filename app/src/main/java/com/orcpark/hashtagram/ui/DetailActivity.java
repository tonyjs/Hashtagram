package com.orcpark.hashtagram.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.InstaItem;
import com.orcpark.hashtagram.ui.widget.SlipLayout;

/**
 * Created by orcpark on 14. 11. 13..
 */
public class DetailActivity extends BaseActivity implements BaseFragment.OnSlipLayoutCreatedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        InstaItem item = getItem();
        addFragment(DetailFragment.newInstance(item));
    }

    private InstaItem getItem() {
        return getIntent() != null ? (InstaItem) getIntent().getSerializableExtra("item") : null;
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
        return R.id.container;
    }

    @Override
    public void onSlipLayoutCreated(SlipLayout slipLayout) {
        slipLayout.setTargetView(mToolBar);
    }
}
