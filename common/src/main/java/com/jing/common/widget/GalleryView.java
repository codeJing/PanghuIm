package com.jing.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jing.common.R;
import com.jing.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;


public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x100;
    private static final int MAX_IMAGE_COUNT = 4;//最大选择的图片数
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//展示的最小图片尺寸
    private LinkedList<Image> mSelectedImages = new LinkedList<>();
    private Adapter mAdapter = new Adapter();
    private LoaderCallback loaderCallback = new LoaderCallback();
    private SelectedChangeListener mListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    //初始化布局
    public void init() {

        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerAdapter.OnRecyclerItemClickImpl<Image>() {
            @Override
            public void onClick(RecyclerAdapter.ViewHolder viewHolder, Image image) {
                super.onClick(viewHolder, image);
                if (onItemSelectClick(image)) {
                    viewHolder.updateData(image);
                }
            }
        });
    }

    /**
     * 设置初始化数据
     * @param load
     */
    public void setLoad(LoaderManager load,SelectedChangeListener listener) {
        load.initLoader(LOADER_ID, null, loaderCallback);
        this.mListener = listener;

    }


    //创建适配器
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected ViewHolder<Image> onCreateViewHolder(android.view.View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }
    }

    //创建viewHolder
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        public void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//  不使用缓存，直接从原图加载
                    .centerCrop()
                    .placeholder(R.color.grey_200) // 默认颜色
                    .into(mPic);

            mShade.setVisibility(image.isSelect?VISIBLE:GONE);
            mSelected.setSelected(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }


    private class Image {
        String path; //图片路径
        int id;//图片id
        long date;//创建创建时间
        boolean isSelect; //是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            if (id != image.id) return false;
            if (date != image.date) return false;
            if (isSelect != image.isSelect) return false;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * Cell点击的具体逻辑
     *
     * @param image Image
     * @return True，代表我进行了数据更改，你需要刷新；反之不刷新
     */
    private boolean onItemSelectClick(Image image) {
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() > MAX_IMAGE_COUNT) {
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }

        }
        if (notifyRefresh)
            notifySelectChanged();
        return notifyRefresh;
    }



    /**
     * 通知Adapter数据更改的方法
     *
     * @param images
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 获取所有选中图片地址
     * @return 返回一个数组
     */
    public  String[] getSelectImages(){
        int index = 0;
        String[] paths = new String[mSelectedImages.size()];
        for (Image image: mSelectedImages){
            paths[index++] =image.path ;
        }

        return paths;
    }


    /**
     * 可以进行清空选中的图片
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            // 一定要先重置状态
            image.isSelect = false;
        }
        mSelectedImages.clear();
        // 通知更新
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        // 得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener = mListener;
        if (listener != null) {
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }


    //LoaderCallBack 的具体实现类
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {


        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID, // Id
                MediaStore.Images.Media.DATA, // 图片路径
                MediaStore.Images.Media.DATE_ADDED // 图片的创建时间ø
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 创建一个Loader
            if (id == LOADER_ID) { // 如果是我们的ID则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + "DESC"); // 倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 当Loader加载完成时
            List<Image> images = new ArrayList<>();
            // 判断是否有数据
            if (data != null) {
                if (data.getCount() > 0) {
                    data.moveToFirst();
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int picId = data.getInt(indexId);
                        String picPath = data.getString(indexPath);
                        long picDateTime = data.getLong(indexDate);

                        File file = new File(picPath);
                        if (file == null || file.length() < MIN_IMAGE_FILE_SIZE) {// 如果没有图片，或者图片大小太小，则跳过
                            continue;
                        }
                        Image image = new Image();
                        image.id = picId;
                        image.path = picPath;
                        image.date = picDateTime;

                        images.add(image);
                    } while (data.moveToNext());

                }

            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // 当Loader销毁或者重置了, 进行界面清空
            updateSource(null);
        }
    }


    /**
     * 对外的一个监听器
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);
    }

}
