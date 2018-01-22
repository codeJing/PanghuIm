package com.jing.panghuim.frags.search;


import com.jing.common.app.BaseFragment;
import com.jing.panghuim.R;
import com.jing.panghuim.activities.SearchActivity;


/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends BaseFragment
        implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
