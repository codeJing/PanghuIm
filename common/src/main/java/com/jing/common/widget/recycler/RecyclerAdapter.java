package com.jing.common.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jing on 2018/1/7.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener, RecyclerAdapterCallBack<Data> {

    private List<Data> mDataList = new ArrayList<>();
    private OnRecyclerItemClick mOnRecyclerItemClick;

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        ViewHolder<Data> viewHolder = onCreateViewHolder(rootView, viewType);
        rootView.setOnClickListener(this);
        rootView.setOnLongClickListener(this);
        rootView.setTag(R.id.tag_recycler_holder);
        viewHolder.unbinder = ButterKnife.bind(viewHolder, rootView);

        viewHolder.mCallBack = this;
        return viewHolder;
    }


    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    protected abstract int getItemViewType(int position, Data data);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mOnRecyclerItemClick != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = holder.getAdapterPosition();
            mOnRecyclerItemClick.onClick(holder, mDataList.get(pos));
        }

    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mOnRecyclerItemClick != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = holder.getAdapterPosition();
            mOnRecyclerItemClick.onLongClick(holder, mDataList.get(pos));
            return true;
        }

        return false;
    }

    //设置点击事件
    protected interface OnRecyclerItemClick<Data> {
        void onClick(RecyclerAdapter.ViewHolder viewHolder, Data data);

        void onLongClick(RecyclerAdapter.ViewHolder viewHolder, Data data);
    }

    public void setOnItemClick(OnRecyclerItemClick<Data> OnRecyclerItemClick) {
        this.mOnRecyclerItemClick = OnRecyclerItemClick;
    }

    //设置viewHolder
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private RecyclerAdapterCallBack<Data> mCallBack;
        protected Data mData;
        private Unbinder unbinder;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void bind(Data mData) {
            this.mData = mData;
            onBind(mData);
        }

        public abstract void onBind(Data mData);

        /**
         * 更新数据
         *
         * @param mData
         */
        public void updateData(Data mData) {
            if (this.mCallBack != null) {
                this.mCallBack.update(mData, this);
            }
        }
    }

    /**
     * 实现update 方法
     *
     * @param data
     * @param viewHolder
     */
    @Override
    public void update(Data data, ViewHolder<Data> viewHolder) {
        // 得到当前ViewHolder的坐标
        int pos = viewHolder.getAdapterPosition();
        if (pos > 0) {
            mDataList.remove(pos);
            mDataList.add(pos, data);
            notifyItemChanged(pos);
        }
    }

    //集合操作

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void addAll(Data... dataList) {
        if (dataList != null || dataList.length > 0) {
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(mDataList.size(), dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void addAll(Collection<Data> dataList) {
        if (dataList != null || dataList.size() > 0) {
            mDataList.addAll(dataList);
            notifyItemRangeInserted(mDataList.size(), dataList.size());
        }
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        if (dataList != null || dataList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

    }


    public void remove(int pos) {
        mDataList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }


    /**
     * 对点击事件的回掉接口做一次实现
     *
     * @param <Data>
     */
    public static abstract class OnRecyclerItemClickImpl<Data> implements OnRecyclerItemClick<Data> {


        @Override
        public void onClick(ViewHolder viewHolder, Data data) {

        }

        @Override
        public void onLongClick(ViewHolder viewHolder, Data data) {

        }
    }

}
