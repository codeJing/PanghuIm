package com.jing.panghuim.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.jing.common.app.BaseActivity;
import com.jing.panghuim.R;
import com.jing.panghuim.account.UpdateInfoFragment;

import butterknife.BindView;

public class UserActivity extends BaseActivity {
    private UpdateInfoFragment mUpDateFragment;
    @BindView(R.id.im_bg)
    ImageView mBg;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }


    /**
     * 显示界面的入口方法
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
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
        mUpDateFragment.onActivityResult(requestCode, resultCode, data);
    }

}
