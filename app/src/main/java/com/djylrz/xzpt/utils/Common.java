package com.djylrz.xzpt.utils;

import android.util.Log;
import android.view.animation.AlphaAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
  *@Description: TODO
  *@Author: mingjun
  *@Date: 2019/5/30 上午 9:29
  */
public class Common {
    private static final String TAG = "Common";

    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void sendOkhttpRequestPost(String address, String username, String name, String password, okhttp3.Callback callback) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                                            .add("username",username)
                                            .add("name",name)
                                            .add("password",password).build();

            Request request = new Request.Builder().url(address).post(requestBody).build();
            client.newCall(request).enqueue(callback);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static AlphaAnimation getAnimation() {
        AlphaAnimation alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
        return alphaAnimationShowIcon;
    }


    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    //接收时间戳，格式化时间并返回
    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有日期数据哦";
    }

    public static String getTime(String time, int timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有数据";
    }

    //得到当前时间回退两天的日期
    public static String getLastDay() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -2);
        Date today = now.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    /**
     * 通过ping判断是否可用
     * @return
     */
    public static boolean ping() {
        try {
            //服务器ip地址
            String ip = "101.132.142.40";
            Log.d(TAG, "ping: 正在ping服务器"+ip);
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ip);
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        }
        catch (IOException e) {}
        catch (InterruptedException e) {}
        return false;
    }

    /**
     * 获取最新版本信息
    * @return
     * @throws IOException
    * @throws org.json.JSONException
    */
    public static String[] getVersion() throws IOException, JSONException {
        String string = null;
        try {
            URL url = new URL("https://serve.wangmingjun.top/app/xzpt/version.json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(8 * 1000);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            string = bufferedReader.readLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(string!=null) {
            //对json数据进行解析
            JSONObject jsonObject = new JSONObject(string);
            String strings[] = new String[3];
            strings[0] = jsonObject.getString("code");
            strings[1] = jsonObject.getString("update");
            strings[2] = jsonObject.getString("content");
            return strings;
        }
        else {
            String strings[] = {"-1","-1","-1"};
            return strings;
        }

    }
}
