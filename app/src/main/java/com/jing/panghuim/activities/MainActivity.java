package com.jing.panghuim.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnTabSelectListener {


    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    ViewPager mViewPager;
    @BindView(R.id.btn_action)
    FloatActionButton mFab;
    @BindView(R.id.navigation)
    Navigation mNavigation;
    //导航栏标签集合
    private ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private String[] mTitles = getResources().getStringArray(R.array.tab_menu);
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
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mNavigation.setOnTabSelectListener(this);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    protected void initData() {
        super.initData();

        mFragmentList.add(new ChatFragment());
        mFragmentList.add(new PhoneFragment());
        mFragmentList.add(new ContactFragment());
        mFragmentList.add(new MyFragment());
        //初始化标签
        for (int i = 0; i < mTitles.length; i++) {
            mTabList.add(new TabEntity(mTitles[i], mTabSelectIds[i], mTabUnSelectIds[i]));
        }
        mNavigation.setTabData(mTabList);
    }


    //viewpager OnPageChangeListener 实现方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        mNavigation.setCurrentTab(position);
    }


    //setOnTabSelectListener 实现方法

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

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

}
