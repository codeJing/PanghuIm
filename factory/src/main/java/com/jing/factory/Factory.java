package com.jing.factory;

import com.jing.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by pc on 2018/1/19.
 */

public class Factory {
    private final Executor mExecutor;
    private static volatile Factory instance;

    private Factory() {
        mExecutor = Executors.newFixedThreadPool(4);
    }

    public Factory getInstance(){

        if (instance == null){
            synchronized (Factory.class){
                if (instance ==null){
                    instance = new Factory();
                }
            }
        }
        return  instance;
    }



    // 拿到单例，拿到线程池，然后异步执行
    public static void runOnAsync(Runnable run) {
        instance.mExecutor.execute(run);
    }


    public  static Application app(){
        return  Application.getInstance();
    }

}
