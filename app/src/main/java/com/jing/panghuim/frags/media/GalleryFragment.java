package com.jing.panghuim.frags.media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jing.common.widget.GalleryView;
import com.jing.panghuim.R;


/**
 * 图片选择Fragment
 */
public class GalleryFragment extends DialogFragment implements GalleryView.SelectedChangeListener {

    private GalleryView mGalleryView;
    private OnSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGalleryView = (GalleryView) root.findViewById(R.id.galleryView);
        //Do something
        // 设置宽度为屏宽、靠近屏幕底部。
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.trans);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return root;
    }



    @Override
    public void onStart() {
        super.onStart();
        mGalleryView.setLoad(getLoaderManager(), this);
    }

    // 重写Gallery获取数量的方法
    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) {
            // 得到所有的选中的图片的路径
            String[] selectImages = mGalleryView.getSelectImages();
            if (mListener != null) {
                mListener.onSelectImage(selectImages[0]);
                // 取消和唤起者之间的应用，加快内存回收
                mListener = null;
            }


        }
    }

    /**
     * 设置事件监听，并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        this.mListener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectImage(String path);

    }


}
