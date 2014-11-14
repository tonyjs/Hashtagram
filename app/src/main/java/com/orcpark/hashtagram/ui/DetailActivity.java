package com.orcpark.hashtagram.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.io.model.insta.InstaItem;

/**
 * Created by orcpark on 14. 11. 13..
 */
public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initLayout();
    }

    private void initLayout() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.drawable.ic_up);
        mToolBar.setTitleTextColor(Color.WHITE);
        Intent intent = getIntent();
        InstaItem item = intent != null ? (InstaItem) intent.getSerializableExtra("item") : null;
        String title = item != null ? item.getUser().getFullName() : null;
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
        addFragment(DetailFragment.newInstance(item));
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
}
