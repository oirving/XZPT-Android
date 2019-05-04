package com.djylrz.xzpt.utils;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class VolleyNetUtil {


    //网络请求使用单例模式
    private static VolleyNetUtil instance;

    //在构造器中实例化保证单一实例
    private RequestQueue requestQueue;



    //首先私有化构造器，让别人不能new出其它实例。
    private VolleyNetUtil(){
    }


    //声明一个公有的方法getInstance提供给调用者本类实例。
    public static VolleyNetUtil getInstance() {
        if (instance == null){
            instance = new VolleyNetUtil();
        }
        return instance;

    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(Context context) {
        this.requestQueue = Volley.newRequestQueue(context); //把上下文context作为参数传递进去
        ;
    }
}
