package com.jing.common.app;

import android.os.SystemClock;

import java.io.File;

/**
 * Created by pc on 2018/1/18.
 */

public class Application extends android.app.Application {


    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 外部获取单例
     * @return
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * 获取头像的临时存储文件地址
     *
     * @return 临时文件
     */
    public static File getPortraitTmpFile() {
        // 得到头像目录的缓存地址
        File dir = new File(instance.getCacheDir(), "portrait");
        // 创建所有的对应的文件夹
        dir.mkdirs();
        //删除旧的缓存文件
        File[] files = dir.listFiles();
        if (files!=null && files.length >0) {
            for (File file : files) {
                file.delete();
            }
        }
        // 返回一个当前时间戳的目录文件地址
        File file = new File(dir, SystemClock.uptimeMillis() + ".jgp");
        return file.getAbsoluteFile();
    }


}
