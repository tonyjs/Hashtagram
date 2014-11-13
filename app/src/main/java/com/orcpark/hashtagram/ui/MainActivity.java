package com.orcpark.hashtagram.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.orcpark.hashtagram.io.db.HashtagramDatabase;
import com.orcpark.hashtagram.io.model.PageRecyclerItem;
import com.orcpark.hashtagram.ui.widget.SlidingTabLayout;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.config.PreferenceConfig;
import com.orcpark.hashtagram.io.OnFinishedListener;
import com.orcpark.hashtagram.io.model.PageItem;
import com.orcpark.hashtagram.ui.adapter.MainViewPagerAdapter;
import com.orcpark.hashtagram.ui.widget.SlipLayout;
import com.orcpark.hashtagram.util.PreferenceUtils;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
                            implements OnFinishedListener, ViewPager.OnPageChangeListener,
                                InstagramRecyclerFragment.Listener, SearchFragment.OnSearchListener{

    public static final String SIGN_IN_FRAGMENT = "SignInFragment";

    private boolean mHasAccessToken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPreferences();
        initLayout();

        String accessToken = PreferenceUtils.getAccessToken(this);
        mHasAccessToken = !TextUtils.isEmpty(accessToken);
        if (mHasAccessToken) {
            initItems();
        } else {
            showSignInFragment();
        }
    }

    private void initPreferences() {
        boolean hasInstalled = PreferenceUtils.hasInstalled(this);
        if (!hasInstalled) {
            HashtagramDatabase.getInstance(this).insert("Newsfeed");
            PreferenceUtils.setHasInstalled(this);
        }
    }

    private ViewPager mViewPager;
    private SlidingTabLayout mTabLayout;

    private MainViewPagerAdapter mViewPagerAdapter;
    private Toolbar mToolBar;
    private void initLayout() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mViewPagerAdapter =
                new MainViewPagerAdapter(getSupportFragmentManager(), this));

        mTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout);
        mTabLayout.setOnPageChangeListener(this);
    }

    private void initItems() {
        ArrayList<String> items = HashtagramDatabase.getInstance(this).getAllHashTag();
        if (items == null || items.size() <= 0) {
            return;
        }

        ArrayList<PageItem> pageItems = new ArrayList<PageItem>();
        int i = 0;
        for (String hashTag : items) {
            pageItems.add(new PageRecyclerItem(i, hashTag));
            i++;
        }

        mViewPagerAdapter.setItems(pageItems);

        mTabLayout.setViewPager(mViewPager);
    }

    private void showSignInFragment() {
        SignInFragment signInFragment = SignInFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, signInFragment, SIGN_IN_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mHasAccessToken) {
            getMenuInflater().inflate(R.menu.main_search, menu);
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    private void handleQuery(String hashTag) {
        HashtagramDatabase.getInstance(this).insert(hashTag);
        if (mViewPagerAdapter != null) {
            mViewPagerAdapter.addItem(new PageRecyclerItem(mViewPagerAdapter.getCount(), hashTag));
            int currentPage = mViewPagerAdapter.getCount();
            mTabLayout.setViewPager(mViewPager);
            mViewPager.setCurrentItem(currentPage - 1, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                            android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(android.R.id.content, new SearchFragment(), "SEARCH_FRAGMENT")
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinished(String accessToken) {
        if (TextUtils.isEmpty(accessToken)) {
            Toast.makeText(this,
                    "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PreferenceConfig.ACCESS_TOKEN, accessToken);
            editor.apply();
            editor.commit();

            Toast.makeText(this,
                    "인증에 성공하였습니다. 환영합니다.", Toast.LENGTH_SHORT).show();

            PreferenceUtils.setAccessToken(this, accessToken);

            mHasAccessToken = true;

            supportInvalidateOptionsMenu();

            initItems();
        }

        getSupportFragmentManager().popBackStackImmediate();
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Log.e("jsp", "onPageSelected");
        if (mViewPagerAdapter == null) {
            return;
        }
//        InstagramFragment fragment = (InstagramFragment) mViewPagerAdapter.getItem(position);
//        fragment.setTargetView(mTabLayout);
        showTargetViewForcibly();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showTargetViewForcibly() {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) getTartgetView().getLayoutParams();
        params.topMargin = 0;
        getTartgetView().setLayoutParams(params);
    }

    public View getTartgetView() {
        return mTabLayout;
    }

    @Override
    public void onAttach(InstagramRecyclerFragment fragment) {

    }

    @Override
    public void onDetach(InstagramRecyclerFragment fragment) {

    }

    @Override
    public void onActivityCreated(InstagramRecyclerFragment fragment) {
        int currentPosition = mViewPager.getCurrentItem();
        int position = fragment.getPosition();
//        if (currentPosition == position) {
            SlipLayout slipLayout = fragment.getSlipLayout();
            slipLayout.setTargetView(getTartgetView());
//        }
    }

    @Override
    public void onSearch(String search) {
        getSupportFragmentManager().popBackStackImmediate();
        handleQuery(search);
    }
}
