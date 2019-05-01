package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;


public class ActorChoose extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ActorChoose";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_choose);

        Button studentButton=(Button) findViewById(R.id.choose_student);//学生登陆按钮

        Button companyButton=(Button) findViewById(R.id.choose_company);//企业登陆按钮

        studentButton.setOnClickListener(this);
        companyButton.setOnClickListener(this);
    }

    //按钮响应事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_student:
                //查询student_token，若student_token存在，使用student_token登录
                SharedPreferences userToken = getSharedPreferences("token",0);
                if (userToken.getString(PostParameterName.STUDENT_TOKEN,null) != null){
                    User user = new User();
                    user.setToken(userToken.getString(PostParameterName.STUDENT_TOKEN,null));
                    studentLoginWithToken(user);
                }else{
                    Intent student = new Intent(ActorChoose.this, StudentLogin.class);
                    startActivity(student);
                    finish();
                    Toast.makeText(ActorChoose.this,"学生用户",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.choose_company:
                Intent company = new Intent(ActorChoose.this, CompanyLogin.class);
                startActivity(company);
                finish();
                Toast.makeText(ActorChoose.this,"企业用户",Toast.LENGTH_SHORT).show();
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
}
