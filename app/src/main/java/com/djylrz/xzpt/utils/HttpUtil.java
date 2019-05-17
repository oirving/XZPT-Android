package com.djylrz.xzpt.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

/**
  *@Description: 异步Http请求工具类
  *@Author: mingjun
  *@Date: 2019/5/17 下午 9:01
  */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static AsyncHttpClient client = new AsyncHttpClient();//实例化对象
    //Host地址
    public static final String HOST_GET = "www.fjrclh.com";
    //福州大学招聘会日历网址
    public static final String FZU_RECRUITMENT_DATE_URL = "http://www.fjrclh.com/calendar/";


    //静态初始化
    static {
        client.setTimeout(10000);//设置链接超时，默认为10s
        //设置请求头
        client.addHeader("Host", HOST_GET);
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");

    }

    /**
     * GET,用一个完整的url获取一个string对象
     *
     * @param urlString
     * @param res
     */
    public static void get(String urlString, AsyncHttpResponseHandler res) {
        client.get(urlString, res);
    }

    /**
     * GET, 带参数请求
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void get(String urlString, RequestParams params,
                           AsyncHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    /**
     * GET,下载数据使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void get(String uString, BinaryHttpResponseHandler bHandler) {
        client.get(uString, bHandler);
    }

    /**
     * POST,不带参数
     *
     * @param urlString
     * @param res
     */
    public static void post(String urlString, AsyncHttpResponseHandler res) {
        client.post(urlString, res);
    }

    /**
     * POST，带参数
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(String urlString, RequestParams params,
                            AsyncHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    /**
     * POST，返回二进制数据时使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void post(String uString, BinaryHttpResponseHandler bHandler) {
        client.post(uString, bHandler);
    }

    /**
     * 返回请求客户端
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        return client;
    }


    /**
     * 接口回调
     */
    public interface QueryCallback {
        public String handleResult(byte[] result);
    }
    //判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
