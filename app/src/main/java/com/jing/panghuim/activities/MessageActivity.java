package com.jing.panghuim.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.jing.common.app.BaseActivity;
import com.jing.factory.model.Author;
import com.jing.panghuim.R;


public class MessageActivity extends BaseActivity {

    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param author  人的信息
     */
    public static void show(Context context, Author author) {

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

}
