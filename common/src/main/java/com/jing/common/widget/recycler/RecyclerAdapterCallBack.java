package com.jing.common.widget.recycler;

/**
 * Created by jing on 2018/1/7.
 */

public interface RecyclerAdapterCallBack<Data> {

    void update(Data data ,RecyclerAdapter.ViewHolder<Data> viewHolder);
}
