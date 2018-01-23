package com.jing.factory.presenter.search;


import com.jing.factory.modle.card.GroupCard;
import com.jing.factory.modle.card.UserCard;
import com.jing.factory.presenter.BaseContract;

import java.util.List;

/**
 * @version 1.0.0
 */
public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }

}
