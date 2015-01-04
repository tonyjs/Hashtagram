package com.orcpark.hashtagram.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.orcpark.hashtagram.io.JsonParser;
import com.orcpark.hashtagram.io.OnFinishedListener;
import com.orcpark.hashtagram.io.db.HashtagramDatabase;
import com.orcpark.hashtagram.io.model.PageItem;
import com.orcpark.hashtagram.io.model.insta.UserInfo;
import com.orcpark.hashtagram.io.request.ResponseListener;
import com.orcpark.hashtagram.ui.widget.SlipLayout;
import com.orcpark.hashtagram.util.PrefUtils;
import com.orcpark.hashtagram.util.RequestFactory;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationFragment.LifecycleCallback, NavigationFragment.NavigationCallback,
                    OnFinishedListener, RecyclerFragment.Listener, SearchFragment.OnSearchListener{

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
            setNavigation();
            initNavigationUi();
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

    @InjectView(R.id.drawer_layout) DrawerLayout mDrawer;
    private void initLayout() {
        ButterKnife.inject(this);

        Toolbar toolbar = getToolBar();
        setSupportActionBar(toolbar);

    }

    private void initNavigationUi() {
        if (mNavigation != null) {
            mNavigation.updateUi();
        }
    }

    private void showSignInFragment() {
        SignInFragment signInFragment = SignInFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, signInFragment, SIGN_IN_FRAGMENT)
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

    private void setNavigation() {
        mToolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.START);
            }
        });
    }

    private void handleQuery(String hashTag) {
        HashtagramDatabase.getInstance(this).insert(hashTag);
        if (mNavigation != null) {
            mNavigation.updateNavigationItems();
            mNavigation.checkLatestNavigation();
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

                                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                                initNavigationUi();

                                setNavigation();
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
        initNavigationUi();
    }

    private void logOut() {
        PrefUtils.removeAccessToken(this);
        PrefUtils.setHasInstalled(this, false);
        HashtagramDatabase.getInstance(this).deleteAll();

        supportInvalidateOptionsMenu();

        initPreferences();
        showSignInFragmentIfNeed();
    }

    private void showTargetViewForcibly() {
        mToolBar.animate()
                .translationY(0)
                .setDuration(150);
    }

    public View getTartgetView() {
        return findViewById(R.id.layout_toolbar);
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
        return R.id.fragment_container;
    }

    private NavigationFragment mNavigation;
    @Override
    public void onFragmentCreated(NavigationFragment fragment) {
        mNavigation = fragment;
        mNavigation.setNavigationCallback(this);
    }

    @Override
    public void onItemSelected(PageItem item) {
        Fragment fragment = item.getFragment();
        replaceFragment(fragment);
        showTargetViewForcibly();
        mDrawer.closeDrawer(Gravity.START);
    }
}
