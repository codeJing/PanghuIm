package com.jing.panghuim.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jing.common.app.BaseActivity;
import com.jing.panghuim.R;
import com.jing.panghuim.account.UpdateInfoFragment;
import com.jing.panghuim.frags.account.AccountTrigger;
import com.jing.panghuim.frags.account.LoginFragment;
import com.jing.panghuim.frags.account.RegisterFragment;

public class AccountActivity extends BaseActivity implements AccountTrigger{

    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }


    public  static  void start(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();

    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment){
            //默认情况下为null，
            //第一次之后就不为null了
            if (mRegisterFragment == null){
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        }else {
            // 因为默认请求下mLoginFragment已经赋值，无须判断null
            fragment = mLoginFragment;
        }
        // 重新赋值当前正在显示的Fragment
            mCurFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container,fragment)
                .commit();
    }
}
