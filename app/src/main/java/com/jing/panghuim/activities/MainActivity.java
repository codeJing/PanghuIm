package com.jing.panghuim.activities;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jing.common.app.BaseActivity;
import com.jing.common.widget.Navigation;
import com.jing.common.widget.PortraitView;
import com.jing.common.widget.TabEntity;
import com.jing.panghuim.R;
import com.jing.panghuim.frags.main.ChatFragment;
import com.jing.panghuim.frags.main.ContactFragment;
import com.jing.panghuim.frags.main.MyFragment;
import com.jing.panghuim.frags.main.PhoneFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnTabSelectListener, View.OnClickListener, EasyPermissions.PermissionCallbacks{

    // 权限回调的标示
    private static final int RC = 0x0100;
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    ViewPager mViewPager;
    @BindView(R.id.btn_action)
    FloatActionButton mFaBtn;
    @BindView(R.id.navigation)
    Navigation mNavigation;
    //导航栏标签集合
    private ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private String[] mTitles;
    //设置点击与未点击图标
    private int[] mTabUnSelectIds = {
            R.drawable.tab_home_unselect, R.drawable.tab_speech_unselect,
            R.drawable.tab_contact_unselect, R.drawable.tab_more_unselect};
    private int[] mTabSelectIds = {
            R.drawable.tab_home_select, R.drawable.tab_speech_select,
            R.drawable.tab_contact_select, R.drawable.tab_more_select};

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavigation.setOnTabSelectListener(this);
        mViewPager.addOnPageChangeListener(this);
        mFaBtn.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        super.initData();
        mTitles = getResources().getStringArray(R.array.tab_menu);
        mFragmentList.add(new ChatFragment());
        mFragmentList.add(new PhoneFragment());
        mFragmentList.add(new ContactFragment());
        mFragmentList.add(new MyFragment());
        //初始化标签
        for (int i = 0; i < mTitles.length; i++) {
            mTabList.add(new TabEntity(mTitles[i], mTabSelectIds[i], mTabUnSelectIds[i]));
        }
        //设置导航栏与viewpager
        mNavigation.setTabData(mTabList);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        requestPerm();
    }


    //viewpager 监听的实现方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {

        float transY = 0;
        mNavigation.setCurrentTab(position);
        if (position == 1) {
            mFaBtn.setImageResource(R.drawable.ic_contact_add);
        } else {
            transY = Ui.dipToPx(getResources(), 76);
        }

        mFaBtn.animate().translationY(transY).
                setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();


    }


    //Navigation 点击事件监听的 实现方法

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    //floatButton
    @Override
    public void onClick(View view) {
        AccountActivity.start(this);
    }



    private class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog
//                    .Builder(this)
//                    .build()
//                    .show();
//        }
    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {

        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    RC, perms);
        }
    }

}
