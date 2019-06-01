package com.djylrz.xzpt.activity.company;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.activity.student.ForgetPasswordActivity;
import com.djylrz.xzpt.bean.Company;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class CompanyLogin extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CompanyLogin";

    private EditText id;//接收账号
    private EditText password;//接收密码
    private ImageView headPortrait;//头像
    private Company company = new Company();//企业实体对象
    private String responseData;
    private String token;
    private RequestQueue requestQueue;
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
                Intent forgetPassword = new Intent(CompanyLogin.this, ForgetPasswordActivity.class);//跳到忘记密码
                forgetPassword.putExtra("TYPE","company");
                startActivity(forgetPassword);
                RxToast.info("忘记密码");
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
            if (responseData != null) {
                switch (result.getResultCode()) {
                    case "200": {
                        //获取企业token，并保存到SharedPreferences
                        Company company = new Company();
                        company.setToken(result.getResultObject());
                        SharedPreferences companyToken = getSharedPreferences("token", 0);
                        SharedPreferences.Editor editor = companyToken.edit();
                        editor.putString(PostParameterName.TOKEN, company.getToken());
                        editor.commit();

                        //已经验证企业用户名密码正确，请在下面实现企业用户登录成功后的界面跳转
                        //企业登录成功界面暂无
                        Log.d(TAG, "onPostExecute: 企业用户登录成功！");
                        MyApplication.setUserType(0);
                        getCompanyInfo();
                        //跳转到企业首页
                        Intent intent = new Intent(CompanyLogin.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case "2008": {
                        //用户名密码有误
                        RxToast.error("用户名或密码错误！");
//                        Toast.makeText(CompanyLogin.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    default: {
                        //未知错误
                        RxToast.error("服务器罢工了，请稍后再试！错误码：" + result.getResultCode());
//                        Toast.makeText(CompanyLogin.this, "登录失败，错误码：" + result.getResultCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                RxToast.error("服务器连接失败，请检查网络连接！");
                Log.d(TAG, "onPostExecute: response is empty");
            }

        }
    }

    private void getCompanyInfo(){
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token",0);
        token = userToken.getString(PostParameterName.TOKEN,null);
        if (token != null){
            Log.d(TAG, "onCreate: TOKEN is "+token);
            company.setToken(token);
            try {
                Log.d(TAG, "onCreate: 获取企业信息，只填了token"+new Gson().toJson(company));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_COMPANY_BY_TOKEN + company.getToken(),new JSONObject(new Gson().toJson(company)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回"+response.toString());
                                Type jsonType = new TypeToken<TempResponseData<Company>>() {}.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<Company> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: "+postResult.getResultCode());
                                company = postResult.getResultObject();
                                company.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("companyUser", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：："+response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("company", new Gson().toJson(company));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //设置全局userid
                                        MyApplication.userId = company.getCompanyId();
                                        //发消息给DemoHandler，设置mipush别名为userid
                                        Message msg = Message.obtain();
                                        msg.what = MyApplication.GET_USER_ID_SUCCESS;
                                        MyApplication.getHandler().sendMessage(msg);
                                        Log.d(TAG, "获取companyId成功");
                                    }
                                });
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }});
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}