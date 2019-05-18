package com.djylrz.xzpt.activityStudent;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentLogin extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StudentLogin";
    private EditText id;//接收账号
    private EditText password;//接收密码
    private ImageView headPortrait;//头像

    private String responseData;
    private User user = new User();//用户实体对象
    private String token;
    private RequestQueue requestQueue;
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
                new LoginAsyncTask().execute();
                break;
            case R.id.student_forget_password_button:
                Intent forgetPassword = new Intent(StudentLogin.this,ForgetPasswordActivity.class);//跳到忘记密码
                startActivity(forgetPassword);
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
            User user = new User();
            user.setEmail(id.getText().toString());
            user.setPasswd(password.getText().toString());

            Log.d(TAG, "doInBackground: GSON string is "+new Gson().toJson(user));

            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的jso串)nnnn
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(user));
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
                    Log.d(TAG,"doInBackground: the response data is "+responseData);
                }else{
                    Log.d(TAG, "doInBackground: POST FAILD");
                    responseData="登录请求失败！";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseData;
        }

        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            if (responseData != null){
                PostResult result = new Gson().fromJson(responseData, PostResult.class);
                switch (result.getResultCode()){
                    case "200":{
                        //获取学生用户token，并保存到SharedPreferences
                        User user = new User();
                        user.setToken(result.getResultObject());
                        SharedPreferences companyToken = getSharedPreferences("token", 0);
                        SharedPreferences.Editor editor = companyToken.edit();
                        editor.putString(PostParameterName.STUDENT_TOKEN,user.getToken());
                        editor.commit();
                        //跳转到用户主界面
                        Intent intent = new Intent(StudentLogin.this,MainActivity.class);
                        Log.d(TAG, "postLogin: 学生用户登录成功！");
                        getStudentInfo();
                        startActivity(intent);
                        finish();
                    }break;
                    case "2008":{
                        //用户名密码有误
                        Toast.makeText(StudentLogin.this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                    }break;
                    default:{
                        //未知错误
                        Toast.makeText(StudentLogin.this,"登录失败，错误码："+result.getResultCode(),Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                Log.d(TAG, "onPostExecute: response is empty");
            }

        }
    }
    private void getStudentInfo(){
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token",0);
        token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        if (token != null){
            Log.d(TAG, "onCreate: TOKEN is "+token);

            user.setToken(token);

            try {
                Log.d(TAG, "onCreate: 获取个人信息，只填了token"+new Gson().toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN+user.getToken(),new JSONObject(new Gson().toJson(user)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回"+response.toString());
                                Type jsonType = new TypeToken<TempResponseData<User>>() {}.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<User> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: "+postResult.getResultCode());
                                user = postResult.getResultObject();
                                user.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：："+response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("student", new Gson().toJson(user));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //设置全局userid
                                        MyApplication.userId = user.getUserId();
                                        //发消息给DemoHandler，设置mipush别名为userid
                                        Message msg = Message.obtain();
                                        msg.what = MyApplication.GET_USER_ID_SUCCESS;
                                        MyApplication.getHandler().sendMessage(msg);
                                        Log.d(TAG, "获取userid成功");
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


