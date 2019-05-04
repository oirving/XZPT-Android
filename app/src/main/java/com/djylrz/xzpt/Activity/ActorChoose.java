package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Company;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;


public class ActorChoose extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ActorChoose";

    LinearLayout layoutStu;
    LinearLayout layoutCompany;
    ImageView imageViewStu;
    ImageView imageViewCompany;
    Button btnStart;
    private static final int GREY = Color.rgb(245,245,245);
    private static final int CHOOSE_NONE = -1;
    private static final int CHOOSE_STU = 1;
    private static final int CHOOSE_COMPANY = 2;
    private int chooseFlag=CHOOSE_NONE;

    private String userToken;
    private String companyToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //验证是否已经登录
        SharedPreferences preferences = getSharedPreferences(PostParameterName.TOKEN,0);
        userToken = preferences.getString(PostParameterName.STUDENT_TOKEN,null);
        companyToken = preferences.getString(PostParameterName.TOKEN,null);

        setContentView(R.layout.activity_actor_choose);


        layoutCompany = findViewById(R.id.linearLayoutCompany);//学生登陆按钮
        layoutStu = findViewById(R.id.linearLayoutStu);//企业登陆按钮
        imageViewStu = findViewById(R.id.imgViewStu);
        imageViewCompany = findViewById(R.id.imgViewCompany);
        btnStart = findViewById(R.id.btnStart);

        layoutCompany.setOnClickListener(this);
        layoutStu.setOnClickListener(this);
        btnStart.setOnClickListener(this);
    }

    //按钮响应事件
    @Override
    public void onClick(View v) {


        imageViewStu.setBackgroundColor(Color.TRANSPARENT);
        imageViewCompany.setBackgroundColor(Color.TRANSPARENT);

        switch (v.getId()) {
            case R.id.linearLayoutStu:

                chooseFlag = CHOOSE_STU;
                imageViewStu.setBackgroundColor(GREY);
                btnStart.setEnabled(true);
                break;

            case R.id.linearLayoutCompany:

                chooseFlag = CHOOSE_COMPANY;
                imageViewCompany.setBackgroundColor(GREY);
                btnStart.setEnabled(true);
                break;

            case R.id.btnStart:

                if(chooseFlag==CHOOSE_STU) {

                    if (userToken != null) {
                        User user = new User();
                        user.setToken(userToken);
                        studentLoginWithToken(user);
                    } else {
                        studentLogin();
                    }
                }else if(chooseFlag==CHOOSE_COMPANY){

                    if(companyToken != null){
                        Company company = new Company();
                        company.setToken(companyToken);
                        companyLoginWithToken(company);
                    }else{
                        companyLogin();
                    }
                }
                break;
            default:
                break;
        }
    }


    private void studentLoginWithToken(User user){
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue

        try {
            Log.d(TAG, "onCreate: 使用token登录"+new Gson().toJson(user));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_LOGIN_WITH_TOKEN+user.getToken(),
                    new JSONObject(new Gson().toJson(user)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()){
                                        case "200":{
                                            //跳转到用户主界面
                                            Intent intent = new Intent(ActorChoose.this,MainActivity.class);
                                            Log.d(TAG, "postLogin: 学生用户登录成功！");
                                            startActivity(intent);
                                            finish();
                                        }break;
                                        default:{
                                            Toast.makeText(ActorChoose.this, "使用token登录失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "run: 使用token登录失败，跳转用户名密码登录"+postResult.getResultCode());
                                            studentLogin();
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }});
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void companyLoginWithToken(final Company company){
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue

        try {
            Log.d(TAG, "onCreate: 使用token登录"+new Gson().toJson(company));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_COMPANY_LOGIN_WITH_TOKEN+company.getToken(),
                    new JSONObject(new Gson().toJson(company)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()){
                                        case "200":{
                                            //跳转到企业首页
                                            Intent intent = new Intent(ActorChoose.this,Main2Activity.class);
                                            startActivity(intent);
                                            Log.d(TAG, "postLogin: 企业用户登录成功！");
                                            finish();

                                        }break;
                                        default:{
                                            Toast.makeText(ActorChoose.this, "使用token登录失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "run: 使用token登录失败，跳转用户名密码登录"+postResult.getResultCode());
                                            companyLogin();
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }});
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //学生端使用用户名密码登录
    private void studentLogin(){
        Intent student = new Intent(ActorChoose.this, StudentLogin.class);
        startActivity(student);
        finish();
        Toast.makeText(ActorChoose.this,"学生用户",Toast.LENGTH_SHORT).show();
    }

    //企业端使用用户名和密码登录
    private void companyLogin(){
        Intent company = new Intent(ActorChoose.this, CompanyLogin.class);
        startActivity(company);
        finish();
        Toast.makeText(ActorChoose.this,"企业用户",Toast.LENGTH_SHORT).show();
    }
}
