package com.jing.panghuim.account;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.jing.common.app.BaseFragment;
import com.jing.common.widget.PortraitView;
import com.jing.factory.Factory;
import com.jing.factory.net.UploadHelper;
import com.jing.panghuim.App;
import com.jing.panghuim.R;
import com.jing.panghuim.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;


public class UpdateInfoFragment extends BaseFragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @Override
    protected void initData() {
        super.initData();

        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectImage(String path) {
                if (!TextUtils.isEmpty(path)) {
                    UCrop.Options options = new UCrop.Options();
                    // 设置图片处理的格式JPEG
                    options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    // 设置压缩后的图片精度
                    options.setCompressionQuality(96);

                    File tmpFile = App.getPortraitTmpFile();

                    UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(tmpFile))
                            .withAspectRatio(1, 1) // 1比1比例
                            .withMaxResultSize(520, 520) // 返回最大的尺寸
                            .withOptions(options) // 相关参数
                            .start(getActivity());
                    Log.i("UpdateInfoFragment", ">> uri.fromFile is " + Uri.fromFile(new File(path)));
                    Log.i("UpdateInfoFragment", ">> uri.parse is " + Uri.parse(path));
                    Log.i("UpdateInfoFragment", ">> uri.path is " + path);
                }
            }
            // show 的时候建议使用getChildFragmentManager，
            // tag GalleryFragment class 名
        }).show(getChildFragmentManager(), UpdateInfoFragment.class.getName());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {

        Glide.with(getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

       final String localPath = uri.getPath();

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.e("TAG", "url:" + url);
            }
        });

    }


}
