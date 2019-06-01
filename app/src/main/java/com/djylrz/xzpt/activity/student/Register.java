package com.djylrz.xzpt.activity.student;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;

import com.vondear.rxtool.view.RxToast;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Register extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Register";

    private EditText mail;//邮箱
    private EditText password;//密码
    private EditText passwordCheck;//确认密码

    private String verificationCode;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mail = (EditText)findViewById(R.id.register_mail);//注册邮件

        password = (EditText)findViewById(R.id.register_password);//注册密码

        passwordCheck = (EditText)findViewById(R.id.register_password_check);//确认密码

        Button register = (Button) findViewById(R.id.register_button);//注册按钮
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String mail=this.mail.getText().toString();//邮件地址
        final String password=this.password.getText().toString();//密码
        String passwordCheck=this.passwordCheck.getText().toString();

        if(mailChek(mail)&&passwordCheck(password,passwordCheck)) {
            //基本验证通过
            //提交请求发送验证到个人邮件
            new GetVerificationCodeAsyncTask().execute();
            //使用dialog接收输入验证码
            final EditText et = new EditText(Register.this);//输入验证码
            AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
            dialog.setTitle("请输入验证码");
            dialog.setView(et);
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!et.getText().toString().equals("")) {//验证码不为空
                        verificationCode=et.getText().toString();//获取输入的验证码
                        new RegisterAsyncTask().execute();//注册
                    } else {
                        RxToast.warning("请输入验证码");
                        dialog.dismiss();
                    }


                }
            });
            dialog.show();
        }
        else if(!mailChek(mail)) {
            RxToast.warning("邮箱错误");
        }
        else if(!passwordCheck(password,passwordCheck)) {
            RxToast.warning("两次输入的密码不同或为空");
        }
    }

    public boolean mailChek(String mail) {
        //邮件格式检查
        if(mail.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean passwordCheck (String password,String passwordCheck) {
        //密码长度和两次输入检查
        if(password.length()>=8&&password.equals(passwordCheck))
            return true;
        else
            return false;
    }

    //获取验证码
    private class GetVerificationCodeAsyncTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            //声明传递的JSON串
            User user = new User();
            user.setEmail(mail.getText().toString());

            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)////获取验证码时间比较长，需要设置timeout
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            String responseData = "";
            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(user));
            //创建一个请求对象
            Request request = new Request.Builder()
                    .url(PostParameterName.POST_URL_GETVERIFICATIONCODE)
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if(response.isSuccessful()){
                    //获取验证码测试-打印服务端返回结果
                    responseData=response.body().string();
                    Log.d(TAG,responseData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseData;
        }

        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            PostResult result = new Gson().fromJson(responseData,PostResult.class);
            switch (result.getResultCode()){
                case "200":{
                    //获取验证码成功，验证码已发送至到邮箱
                    Log.d(TAG, "getVerificationCode: 发送验证码成功");
                    RxToast.info("已发送验证码至邮件");

                }break;
                default:{
                    //未知错误
                    RxToast.error("验证码获取失败，错误码："+result.getResultCode());
                }
            }
        }
    }


    private class RegisterAsyncTask extends AsyncTask<String , Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            //声明传递的JSON串
            User user = new User();
            user.setEmail(mail.getText().toString());
            user.setPasswd(password.getText().toString());

            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(user));
            //创建一个请求对象
            Request request = new Request.Builder()
                    .url(PostParameterName.POST_URL_REGISTER+verificationCode)//url传参：验证码
                    .post(requestBody)
                    .build();
            Log.d(TAG, "register: "+request.toString());

            String responseData="";
            //发送请求获取响应
            try {
                Response response=okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if(response.isSuccessful()){
                    //获取验证码测试-打印服务端返回结果（测试返回结果是否为JSON）
                    responseData=response.body().string();
                    Log.d(TAG,responseData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseData;
        }

        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            PostResult result = new Gson().fromJson(responseData,PostResult.class);
            switch (result.getResultCode()){
                case "200":{
                    //注册成功
                    Log.d(TAG, "getVerificationCode: 注册成功");
                    //跳转到登录界面
                    Intent intent = new Intent(Register.this, StudentLogin.class);
                    startActivity(intent);
                }break;
                default:{
                    //未知错误
                    RxToast.error("注册失败，错误码："
                            +result.getResultCode()
                            +result.getResultMsg());
                }
            }
        }
    }

}
