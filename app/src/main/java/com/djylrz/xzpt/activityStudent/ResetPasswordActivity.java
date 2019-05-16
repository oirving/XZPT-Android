package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
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
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ResetPasswordActivity";

    private EditText verificationCode;
    private EditText password;
    private EditText password2;
    private Button confirm;

    private User user = new User();
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        verificationCode = (EditText) findViewById(R.id.verification_code);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        confirm = (Button) findViewById(R.id.find_back_button);
        confirm.setOnClickListener(this);

        //从上级界面获得邮箱
        user.setEmail(getIntent().getStringExtra(PostParameterName.REQUEST_EMAIL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_back_button:

                if (password.getText().toString().equals(password2.getText().toString())){//两次密码相同

                    user.setPasswd(password.getText().toString());//邮箱密码均设置完成
                    try {
                        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去

                        //组装URL
                        String url = PostParameterName.POST_URL_RESET_PASSWORD+
                                PostParameterName.REQUEST_CODE+"="+verificationCode.getText().toString()+"&"+
                                PostParameterName.REQUEST_EMAIL+"="+user.getEmail()+"&"+
                                PostParameterName.REQUEST_PASSWORD+"="+user.getPasswd();

                        Log.d(TAG, "onClick: GSON"+new Gson().toJson(user));
                        Log.d(TAG, "onClick: JSONOBJECT"+ new JSONObject(new Gson().toJson(user)));
                        Log.d(TAG, "onClick: URL "+url);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                                new JSONObject(new Gson().toJson(user)),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, "onResponse: 返回"+response.toString());
                                        final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(TAG, "run: RESULT CODE : "+postResult.getResultCode());
                                                switch (postResult.getResultCode()){
                                                    case "200":{
                                                        //成功后的跳转
                                                        Toast.makeText(ResetPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ResetPasswordActivity.this,StudentLogin.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }break;
                                                    default : {
                                                        Toast.makeText(ResetPasswordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                                                    }
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
                }
                else {
                    Toast.makeText(this, "两次密码不相同，请重试", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
