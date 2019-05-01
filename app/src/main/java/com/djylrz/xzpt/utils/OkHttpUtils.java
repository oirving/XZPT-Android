package com.djylrz.xzpt.utils;

import android.os.Looper;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class OkHttpUtils {


    //网络请求使用单例模式
    private static OkHttpUtils instance;

    // 必须要用的okhttpclient实例,在构造器中实例化保证单一实例
    private OkHttpClient okHttpClient;



    //首先私有化构造器，让别人不能new出其它实例。
    private OkHttpUtils(){
        //创建一个OkHttpClient对象
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)////获取验证码时间比较长，需要设置timeout
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }


    //声明一个公有的方法getInstance提供给调用者本类实例。
    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }

        return instance;
    }



    /**
     * POST方式构建Request {json}
     * @param url
     * @param json
     * @return
     */
    public static Request bulidRequestForPostByJson(String url, String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        return new Request.Builder()
                .url(url)
                .post(body)
                .build();


    }


}
