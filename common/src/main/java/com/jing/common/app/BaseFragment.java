package com.jing.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by jing on 2018/1/7.
 */

public abstract class BaseFragment extends Fragment {

    private View mRoot;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            View root = inflater.inflate(getContentLayoutId(), container, false);
            initWidget(root);
            mRoot = root;
        }else{
            if (mRoot.getParent() !=null){
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

    }

    protected  abstract  int getContentLayoutId();


    protected  void initArgs(Bundle bundle){

    }

    protected  void initWidget(View root){
        ButterKnife.bind(this,root);
    }

    protected  void initData(){

    }

    /**
     * fragment 是否拦截点击
     * @return
     */
    public boolean onBackPressed(){
        return  false;
    }

}
