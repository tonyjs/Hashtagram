package com.tonyjs.hashtagram.ui;

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
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.tonyjs.hashtagram.config.InstaConfig;
import com.tonyjs.hashtagram.io.model.HostInfo;
import com.tonyjs.hashtagram.io.request.retrofit.HostInfoBody;
import com.tonyjs.hashtagram.io.request.retrofit.HostInfoResponse;

import com.tonyjs.hashtagram.R;
import com.tonyjs.hashtagram.io.OnFinishedListener;
import com.tonyjs.hashtagram.io.db.HashtagramDatabase;
import com.tonyjs.hashtagram.io.model.PageItem;
import com.tonyjs.hashtagram.io.model.insta.UserInfo;
import com.tonyjs.hashtagram.io.request.retrofit.RequestInterface;
import com.tonyjs.hashtagram.io.request.retrofit.RequestTask;
import com.tonyjs.hashtagram.io.request.volley.RequestFactory;
import com.tonyjs.hashtagram.ui.widget.SlipLayout;
import com.tonyjs.hashtagram.util.PrefUtils;
import com.tonyjs.hashtagram.util.ToastUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.FormUrlEncoded;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationFragment.LifecycleCallback, NavigationFragment.NavigationCallback,
                    RecyclerFragment.LifecycleCallback, SearchFragment.OnSearchListener, OnFinishedListener{

    public static final String FRAGMENT_SIGN_IN = "SignInFragment";
    public static final String RECYCLER_FRAGMENT = "RecyclerFragment";
    public static final String NEWSFEED = "Newsfeed";
    public static final String EDIT_HASHTAG_FRAGMENT = "EDIT_HASHTAG_FRAGMENT";
    public static final String SEARCH_FRAGMENT = "SEARCH_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPreferences();
        initLayout();

        showSignInFragmentIfNeed();
    }

    private void showSignInFragmentIfNeed() {
        if (!TextUtils.isEmpty(PrefUtils.getAccessToken(this))) {
            setNavigation();
            initNavigationUi();
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            showSignInFragment();
        }
    }

    private void initPreferences() {
        if (PrefUtils.hasInstalled(this)) {
            return;
        }
        HashtagramDatabase.getInstance(this).insert(NEWSFEED);
        PrefUtils.setHasInstalled(this, true);
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
        addFragment(android.R.id.content, signInFragment, FRAGMENT_SIGN_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(PrefUtils.getAccessToken(this))) {
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
            ToastUtils.toast(this, getString(R.string.authorize_fail));
            return;
        }

        detachSignInFragment();

        Log.d("jsp", "code = " + code);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("인증 중입니다...");
        dialog.show();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(RequestInterface.END_POINT)
                .build();
        RequestInterface requestInterface = restAdapter.create(RequestInterface.class);
        requestInterface.getHostInfo(
                encodeString(code),
                encodeString(InstaConfig.INSTA_CLIENT_ID),
                encodeString(InstaConfig.INSTA_CLIENT_ID),
                encodeString("authorization_code"),
                encodeString(InstaConfig.INSTA_REDIRECT_URI),
                new Callback<HostInfoResponse>() {
                    @Override
                    public void success(HostInfoResponse hostInfoResponse, Response response) {
                        dialog.dismiss();
                        ToastUtils.toast(getApplicationContext(), hostInfoResponse.toString());
                        HostInfo hostInfo = hostInfoResponse.getHostInfo();
                        handleResponse(hostInfo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dialog.dismiss();
                        String errorString =
                                error.getMessage() + "\n" +
                                        error.getBody() + "\n" +
                                        error.getKind() + "\n";
                        Log.e("jsp", errorString);
                        ToastUtils.toast(getApplicationContext(), errorString);
                    }
                });

//        RequestFactory.getUser(this, code, dialog,
//                new ResponseListener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        UserInfo userInfo = JsonParser.getUserInfo(response);
//                        handleResponse(userInfo);
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),
//                                getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @FormUrlEncoded
    private String encodeString(String target) {
        return target;
    }

    private String getEncodedString(String target) {
        try {
            target = URLEncoder.encode(target, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }

    private void detachSignInFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(FRAGMENT_SIGN_IN);
        if (fragment != null) {
            fm.beginTransaction().detach(fragment).commitAllowingStateLoss();
        }
    }

    private void handleResponse(UserInfo userInfo) {
        if (userInfo == null) {
            ToastUtils.toast(this, getString(R.string.authorize_fail));
            return;
        }

        Log.e("jsp", userInfo.toString());

        ToastUtils.toast(this, getString(R.string.authorize_success));

        PrefUtils.setUserInfo(getApplicationContext(), userInfo);

        supportInvalidateOptionsMenu();

        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        initNavigationUi();

        setNavigation();
    }

    private void handleResponse(HostInfo userInfo) {
        if (userInfo == null) {
            ToastUtils.toast(this, getString(R.string.authorize_fail));
            return;
        }

        Log.e("jsp", userInfo.toString());

        ToastUtils.toast(this, getString(R.string.authorize_success));

        PrefUtils.setHostInfo(getApplicationContext(), userInfo);

        supportInvalidateOptionsMenu();

        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        initNavigationUi();

        setNavigation();
    }

    private void detachRecyclerFragment() {
        detachFragment(RECYCLER_FRAGMENT);
    }

    private void showEditHashtagFragment() {
        ArrayList<String> items = HashtagramDatabase.getInstance(this).getAllHashTag();
        if (items == null || items.size() < 2) {
            ToastUtils.toast(this, getString(R.string.no_hashtag));
            return;
        }
        addFragment(android.R.id.content, new EditHashtagFragment(), EDIT_HASHTAG_FRAGMENT, null,
                android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        popBackStack();
        initNavigationUi();
    }

    private void logOut() {
        PrefUtils.removeAccessToken(this);
        PrefUtils.setHasInstalled(this, false);
        HashtagramDatabase.getInstance(this).deleteAll();

        detachRecyclerFragment();

        supportInvalidateOptionsMenu();

        initPreferences();
        showSignInFragmentIfNeed();
    }

    public View getTargetView() {
        return findViewById(R.id.layout_toolbar);
    }

    @Override
    public void onFragmentCreated(RecyclerFragment fragment) {
        SlipLayout slipLayout = fragment.getSlipLayout();
        slipLayout.setTargetView(getTargetView());
        slipLayout.showTargetViewForcibly();
    }

    @Override
    public void onResume(RecyclerFragment fragment) {
        mToolBar.setTitle(fragment.getHashTag());
    }

    private void showSearchFragment() {
        addFragment(android.R.id.content, new SearchFragment(), SEARCH_FRAGMENT, null,
                android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onSearch(String search) {
        getSupportFragmentManager().popBackStackImmediate();
        handleQuery(search);
    }

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
        replaceFragment(fragment, RECYCLER_FRAGMENT);
        mDrawer.closeDrawer(Gravity.START);
    }

}
