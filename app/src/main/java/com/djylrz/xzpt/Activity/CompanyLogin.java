package com.djylrz.xzpt.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.djylrz.xzpt.bean.Company;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.R;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class CompanyLogin extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CompanyLogin";

    private EditText id;//接收账号
    private EditText password;//接收密码
    private ImageView headPortrait;//头像

    private String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

//        //验证是否已经登录
//        SharedPreferences preferences = getSharedPreferences("token",0);
//        String token = preferences.getString(PostParameterName.TOKEN,"");
//        if(token != null){
//            Log.d(TAG, "onCreate: 已登录用户，token为："+token);
//            //跳转到企业首页
//            Intent intent = new Intent(CompanyLogin.this,Main2Activity.class);
//            startActivity(intent);
//            finish();
//        }

        id = (EditText)findViewById(R.id.company_input_name);//输入的账号
        password = (EditText)findViewById(R.id.company_input_password);//输入的密码
        headPortrait = (ImageView)findViewById(R.id.company_head_portrait);//头像

        Button login = (Button)findViewById(R.id.company_login_button);//登陆按钮
        login.setOnClickListener(this);

        Button forgetPassword = (Button)findViewById(R.id.company_forget_password_button);//忘记密码按钮
        forgetPassword.setOnClickListener(this);

        Button back = (Button)findViewById(R.id.company_back_button);//取消按钮
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //按钮响应事件
        switch (v.getId()) {
            case R.id.company_login_button:
                new LoginAsyncTask().execute();
                break;
            case R.id.company_forget_password_button:
                //Intent forgetPassword = new Intent();//跳到忘记密码
                //startActivity(forgetPassword);
                break;
            case R.id.company_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    //可以用于从其他活动接收账号和密码
    public static void actionStart(Context context, String id, String password) {
        Intent intent = new Intent(context,CompanyLogin.class);
        intent.putExtra("id",id);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }

    class LoginAsyncTask extends AsyncTask<String, Integer ,String>{

        @Override
        protected String doInBackground(String... strings) {
            //声明传递的JSON串
            Company company = new Company();
            company.setEmail(id.getText().toString());
            company.setPasswd(password.getText().toString());
            Log.d(TAG, "doInBackground: company json is "+new Gson().toJson(company));

            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(company));
            //创建一个请求对象
            Request request = new Request.Builder()
                    .url(PostParameterName.POST_URL_COMPANY_LOGIN)
                    .post(requestBody)
                    .build();
            //发送请求获取响应
            try {
                Response response=okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if(response.isSuccessful()){
                    responseData = response.body().string();
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
                    //获取企业token，并保存到SharedPreferences
                    Company company = new Company();
                    company.setToken(result.getResultObject());
                    SharedPreferences companyToken = getSharedPreferences("token", 0);
                    SharedPreferences.Editor editor = companyToken.edit();
                    editor.putString(PostParameterName.TOKEN,company.getToken());
                    editor.commit();

                    //TODO：已经验证企业用户名密码正确，请在下面实现企业用户登录成功后的界面跳转——to欧文
                    //企业登录成功界面暂无
                    Log.d(TAG, "onPostExecute: 企业用户登录成功！");
                    //跳转到企业首页
                    Intent intent = new Intent(CompanyLogin.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }break;
                case "2008":{
                    //用户名密码有误
                    Toast.makeText(CompanyLogin.this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                }break;
                default:{
                    //未知错误
                    Toast.makeText(CompanyLogin.this,"登录失败，错误码："+result.getResultCode(),Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}