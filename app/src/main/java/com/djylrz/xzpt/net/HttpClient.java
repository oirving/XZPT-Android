package com.djylrz.xzpt.net;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.djylrz.xzpt.bean.TempResponseData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 封装HTTP，使用方法见TestHttpActivity
 */
public class HttpClient extends OkHttpClient {

    private static final String HOST = "http://101.132.142.40:8080/XZPT-Java-1.0-SNAPSHOT/";
    private static volatile HttpClient instance = null;
    private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    private HttpClient(){};

    /**
     *@Description:  单例模式
     *@Created in: 2019/4/30
     *@Author: Murphy
     *@Method_Name:getInstance
     *@Param:[]
     *@Return: MyOkHttp
     **/
    public static HttpClient getInstance(){

        if(instance==null){
            synchronized (HttpClient.class){
                if(instance == null){
                    instance = new HttpClient();
                }
            }
        }

        return instance;
    }

    /**
     *@Description: 获得当前请求的URL
     *@Created in: 2019/4/30
     *@Author: Murphy
     *@Method_Name:generatorUrl
     *@Param:[HOST, url, parameters]
     *@Return:String
     **/
    private String generatorUrl(String netInterface, Map<String, Object> parameters){
        StringBuilder stringBuilder=new StringBuilder(HOST +netInterface);
        if(parameters!=null) {
            stringBuilder.append("?");
            for (String key : parameters.keySet()) {
                stringBuilder.append(key).append("=").append(parameters.get(key)).append("&");
            }
            if (stringBuilder.lastIndexOf("&") == stringBuilder.length() - 1) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        }
        return stringBuilder.toString();
    }

    /**
     *@Description: description
     *@Created in: 2019/4/30
     *@Author: Murphy
     *@Method_Name:post
     *@Param:[netInterface, parameters, object, callback]
     *@Return:void 发起post请求
     **/
    public <E> void post(String netInterface, Map<String,Object> parameters,E object, Callback callback) {
        String url = generatorUrl(netInterface,parameters);
        String body = new Gson().toJson(object);
        final Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, body)).build();
        newCall(request).enqueue(callback);
    }
    public abstract class MyCallBack implements Callback{

        // 上下文，用于发起Toast等
        private Context context;


        /**
         *@Description: 构造函数
         *@Created in: 2019/4/30
         *@Author: Murphy
         *@Method_Name:MyCallBack
         *@Param:[context]
         *@Return:
         **/
        public MyCallBack(Context context){
            this.context = context;
        }

        /**
         *@Description: 在请求失败之后的处理
         *@Created in: 2019/4/30
         *@Author: Murphy
         *@Method_Name:onFailure
         *@Param:[call, e]
         *@Return:void
         **/
        @Override
        public void onFailure(Call call, IOException e) {
            // TODO 网络错误处理
        }

        /**
         *@Description: 网络请求成功时返回处理
         *@Created in: 2019/4/30
         *@Author: Murphy
         *@Method_Name:onResponse
         *@Param:[call, response]
         *@Return:void
         **/
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){

                assert response.body() != null;

                // 获得返回值的JSON字符串
                String json = response.body().string();
                // 通过GSON转化为返回值对象
                TempResponseData responseData = new Gson().fromJson(json, TempResponseData.class);

                assert responseData != null;

                // 子线程中发起Toast需要调用该方法，且只能调用一次
                if(Looper.myLooper() == null) {
                    Looper.prepare();
                }
                // case 返回值
                switch (responseData.getResultCode()){
                    case 200:
                        // 最好将200这种写成常量
                        Toast.makeText(context,"返回值为"+200,Toast.LENGTH_LONG).show();
                        onSuccess(responseData);
                        break;
                        //TODO 在这里捕获所有的异常，并直接处理，只有200时才会进入
                    default:
                        Toast.makeText(context,"返回值为"+responseData.getResultCode(),Toast.LENGTH_LONG).show();
                        break;
                }
                // 将Toast的消息发送出去
                Looper.loop();
            }else {
                //TODO Failed 处理
            }
        }

        protected abstract void onSuccess(TempResponseData responseData); // TODO 在子类中重写此构造函数。

    }

}
