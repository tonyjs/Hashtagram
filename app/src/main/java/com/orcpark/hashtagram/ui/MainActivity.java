package com.orcpark.hashtagram.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.VolleyError;
import com.orcpark.hashtagram.R;
import com.orcpark.hashtagram.config.PreferenceConfig;
import com.orcpark.hashtagram.io.JsonParser;
import com.orcpark.hashtagram.io.OnFinishedListener;
import com.orcpark.hashtagram.io.db.HashtagramDatabase;
import com.orcpark.hashtagram.io.model.PageItem;
import com.orcpark.hashtagram.io.model.PageRecyclerItem;
import com.orcpark.hashtagram.io.model.insta.InstaItem;
import com.orcpark.hashtagram.io.model.insta.UserInfo;
import com.orcpark.hashtagram.io.request.ResponseListener;
import com.orcpark.hashtagram.ui.adapter.BasicRecyclerAdapter;
import com.orcpark.hashtagram.ui.adapter.MainViewPagerAdapter;
import com.orcpark.hashtagram.ui.widget.BasicRecyclerView;
import com.orcpark.hashtagram.ui.widget.SlidingTabLayout;
import com.orcpark.hashtagram.ui.widget.SlipLayout;
import com.orcpark.hashtagram.util.PrefUtils;
import com.orcpark.hashtagram.util.RequestFactory;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
                            implements OnFinishedListener, ViewPager.OnPageChangeListener,
                                        RecyclerFragment.Listener, SearchFragment.OnSearchListener{

    public static final String SIGN_IN_FRAGMENT = "SignInFragment";
    public static final String NEWSFEED = "Newsfeed";

    private boolean mHasAccessToken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPreferences();
        initLayout();

        showSignInFragmentIfNeed();
    }

    private void showSignInFragmentIfNeed() {
        String accessToken = PrefUtils.getAccessToken(this);
        mHasAccessToken = !TextUtils.isEmpty(accessToken);
        if (mHasAccessToken) {
            initItems();
        } else {
            showSignInFragment();
        }
    }

    private void initPreferences() {
        boolean hasInstalled = PrefUtils.hasInstalled(this);
        if (!hasInstalled) {
            HashtagramDatabase.getInstance(this).insert(NEWSFEED);
            PrefUtils.setHasInstalled(this, true);
        }
    }

    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.sliding_tab_layout) SlidingTabLayout mTabLayout;
    private MainViewPagerAdapter mViewPagerAdapter;
    private void initLayout() {
        ButterKnife.inject(this);

        Toolbar toolbar = getToolBar();
        setSupportActionBar(toolbar);

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mViewPagerAdapter =
                new MainViewPagerAdapter(getSupportFragmentManager(), this));

        mTabLayout.setOnPageChangeListener(this);
    }

    private void initItems() {
        ArrayList<String> items = HashtagramDatabase.getInstance(this).getAllHashTag();
        if (items == null || items.size() <= 0) {
            return;
        }

        ArrayList<PageItem> pageItems = new ArrayList<>();
        int i = 0;
        for (String hashTag : items) {
            pageItems.add(new PageRecyclerItem(i, hashTag));
            i++;
        }

        mViewPagerAdapter.setItems(pageItems);

        mTabLayout.setViewPager(mViewPager);
    }

    private void showSignInFragment() {
        SignInFragment signInFragment = SignInFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, signInFragment, SIGN_IN_FRAGMENT)
//                .addToBackStack(null)
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mToolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDrawer.openDrawer(Gravity.START);
//            }
//        });
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
            showSearchFragment();
            return true;
        } else if (id == R.id.action_log_out) {
            logOut();
            return true;
        } else if (id == R.id.action_edit) {
            showEditHashtagFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinished(String code) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this,
                    getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
        } else {
            detachSignInFragment();

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("인증 중입니다...");
            RequestFactory.getUser(this, code, dialog,
                new ResponseListener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserInfo userInfo = JsonParser.getUserInfo(response);
                        if (userInfo != null) {
                            Log.e("jsp", userInfo.toString());

                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.authorize_success), Toast.LENGTH_SHORT).show();

                            PrefUtils.setUserInfo(getApplicationContext(), userInfo);

                            mHasAccessToken = true;

                            supportInvalidateOptionsMenu();

                            initItems();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
                    }
                });
        }

//        getSupportFragmentManager().popBackStackImmediate();
    }

    private void detachSignInFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(SIGN_IN_FRAGMENT);
        if (fragment != null) {
            fm.beginTransaction().detach(fragment).commitAllowingStateLoss();
        }
    }

    private void showEditHashtagFragment() {
        ArrayList<String> items = HashtagramDatabase.getInstance(this).getAllHashTag();
        if (items == null || items.size() < 2) {
            Toast.makeText(this, getString(R.string.no_hashtag), Toast.LENGTH_LONG).show();
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .add(android.R.id.content, new EditHashtagFragment(), "EDIT_HASHTAG_FRAGMENT")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getSupportFragmentManager().popBackStackImmediate();
        initItems();
    }

    private void logOut() {
        PrefUtils.removeAccessToken(this);
        PrefUtils.setHasInstalled(this, false);
        HashtagramDatabase.getInstance(this).deleteAll();
        mViewPagerAdapter.setItems(null);
        mTabLayout.setViewPager(mViewPager);

        supportInvalidateOptionsMenu();

        initPreferences();
        showSignInFragmentIfNeed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mViewPagerAdapter == null) {
            return;
        }
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
    public void onAttach(RecyclerFragment fragment) {

    }

    @Override
    public void onDetach(RecyclerFragment fragment) {

    }

    @Override
    public void onActivityCreated(RecyclerFragment fragment) {
        SlipLayout slipLayout = fragment.getSlipLayout();
        slipLayout.setTargetView(getTartgetView());
    }

    private void showSearchFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .add(android.R.id.content, new SearchFragment(), "SEARCH_FRAGMENT")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onSearch(String search) {
        getSupportFragmentManager().popBackStackImmediate();
        handleQuery(search);
    }

//    @Override
//    public void onItemClick(Object item) {
//        if (item == null) {
//            return;
//        }
//        if (!(item instanceof InstaItem)) {
//            return;
//        }
//
//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra("item", ((InstaItem) item));
//        intent.putExtra("hashtag",
//                mViewPagerAdapter.getPageTitle(mViewPager.getCurrentItem()));
//        startActivity(intent);
//    }

    @Override
    public int getContainerResId() {
        return R.id.container;
    }
}
