package com.jing.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
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

import java.util.LinkedList;

import butterknife.BindView;


public class GalleryView extends RecyclerView {

    private static final int MAX_PIC = 4;//最大选择的图片数
    private LinkedList<Image> mSelectedImages = new LinkedList<>();
    private Adapter mAdapter = new Adapter();


    public GalleryView(Context context) {
        super(context);
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //初始化布局
    public void init() {

        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerAdapter.OnRecyclerItemClickImpl<Image>() {
            @Override
            public void onClick(RecyclerAdapter.ViewHolder viewHolder, Image image) {
                super.onClick(viewHolder, image);
                //TODO 刷新界面
            }
        });
    }

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

        }
    }


    private class Image {
        String path; //图片路径
        int id;//图片id
        long data;//创建时间
        boolean select; //是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            if (id != image.id) return false;
            if (data != image.data) return false;
            if (select != image.select) return false;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }


    private boolean onItemSelectClick(Image image) {

        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.select = false;
            return true;
        } else {
            if (mSelectedImages.size() > MAX_PIC) {
                return false;
            } else {
                mSelectedImages.add(image);
                image.select = true;
                return true;
            }


        }


    }


}
