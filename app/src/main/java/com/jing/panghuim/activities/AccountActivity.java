package com.jing.panghuim.activities;

import android.content.Intent;

import com.jing.common.app.BaseActivity;
import com.jing.panghuim.R;
import com.jing.panghuim.account.UpdateInfoFragment;

public class AccountActivity extends BaseActivity {

    private  UpdateInfoFragment mUpDateFragment;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
         mUpDateFragment = new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, new UpdateInfoFragment()).commit();

    }

    // Activity中收到剪切图片成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            mUpDateFragment.onActivityResult(requestCode,resultCode,data);
    }

}
