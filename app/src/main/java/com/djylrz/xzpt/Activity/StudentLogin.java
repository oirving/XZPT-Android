package com.djylrz.xzpt.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class StudentLogin extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StudentLogin";
    private EditText id;//接收账号
    private EditText password;//接收密码
    private ImageView headPortrait;//头像

    private String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        id = (EditText)findViewById(R.id.student_input_name);//输入的账号
        password = (EditText)findViewById(R.id.student_input_password);//输入的密码
        headPortrait = (ImageView)findViewById(R.id.student_head_portrait);//头像

        Button login = (Button)findViewById(R.id.student_login_button);//登陆按钮
        login.setOnClickListener(this);

        Button forgetPassword = (Button)findViewById(R.id.student_forget_password_button);//忘记密码按钮
        forgetPassword.setOnClickListener(this);

        Button rigister = (Button)findViewById(R.id.student_register_button);//注册按钮
        rigister.setOnClickListener(this);

        Button back = (Button)findViewById(R.id.student_back_button);//取消按钮
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {//按钮响应事件
        switch (v.getId()) {
            case R.id.student_login_button:
                //开发期间直接跳到登陆界面
                Intent intent = new Intent(StudentLogin.this,MainActivity.class);
                startActivity(intent);
                //new LoginAsyncTask().execute();
                break;
            case R.id.student_forget_password_button:
//                Intent forgetPassword = new Intent();//跳到忘记密码
//                startActivity(forgetPassword);
                Toast.makeText(StudentLogin.this,"忘记密码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.student_register_button:
                Intent rigister = new Intent(StudentLogin.this, Register.class);//跳到注册
                startActivity(rigister);
                break;
            case R.id.student_back_button:
                finish();
                break;
        }
    }

    //可以用于从其他活动接收账号和密码
    public static void actionStart(Context context, String id, String password) {
        Intent intent = new Intent(context,StudentLogin.class);
        intent.putExtra("id",id);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
    class LoginAsyncTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            //声明传递的JSON串
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(PostParameterName.REQUEST_EMAIL, id.getText().toString());
            jsonObject.addProperty(PostParameterName.REQUEST_PASSWORD, password.getText().toString());

            Log.d(TAG, "onClick: "+jsonObject.toString());

            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的jso串)nnnn
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            //创建一个请求对象
            Request request = new Request.Builder()
                    .url(PostParameterName.POST_URL_LOGIN)
                    .post(requestBody)
                    .build();
            //发送请求获取响应
            try {
                Response response=okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if(response.isSuccessful()){
                    responseData = response.body().string();//该函数仅能有效调用一次！
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
            JSONObject responseJSON;
            try {
                responseJSON = new JSONObject(responseData);
                switch (responseJSON.getString(PostParameterName.RESPOND_RESULTCODE)){
                    case "200":{
                        //TODO：已经验证学生用户名密码正确，请在下面实现学生登录成功后的界面跳转——to欧文
                        //Done
                        Intent intent = new Intent(StudentLogin.this,MainActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "postLogin: 学生用户登录成功！");
                    }break;
                    case "2008":{
                        //用户名密码有误
                        Toast.makeText(StudentLogin.this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                    }break;
                    default:{
                        //未知错误
                        Toast.makeText(StudentLogin.this,"登录失败，错误码："+responseJSON.getString(PostParameterName.RESPOND_RESULTCODE),Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


