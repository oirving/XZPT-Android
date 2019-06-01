package com.djylrz.xzpt.activity.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;

import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ForgetPasswordActivity";
    private EditText mailAddress;
    private Button findBack;

    private User user = new User();//用户实体对象
    private RequestQueue requestQueue;
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mailAddress = (EditText)findViewById(R.id.mail_address_edittext);//邮箱
        findBack = (Button) findViewById(R.id.find_back_button);//找回按钮
        findBack.setOnClickListener(this);
        type = getIntent().getStringExtra("TYPE");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_back_button:
                try {
                    user.setEmail(mailAddress.getText().toString());//从输入中获得邮箱

                    requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GETVERIFICATIONCODE,new JSONObject(new Gson().toJson(user)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: 返回"+response.toString());
                                    final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                                    Log.d(TAG, "onResponse: "+postResult.getResultCode());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //成功后的跳转
                                            switch (postResult.getResultCode())
                                            {
                                                case "200":{
                                                    RxToast.success("已发送验证码到邮箱");
                                                    Intent intent = new Intent(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                                                    intent.putExtra(PostParameterName.REQUEST_EMAIL,user.getEmail());
                                                    intent.putExtra("TYPE",type);
                                                    startActivity(intent);
                                                    finish();
                                                }break;
                                                default:{
                                                    RxToast.error("请求发送验证码失败");
                                                }
                                                break;
                                            }
                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }});
                    requestQueue.add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            default:
                break;


        }
    }
}
