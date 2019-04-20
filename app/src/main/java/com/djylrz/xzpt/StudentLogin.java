package com.djylrz.xzpt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class StudentLogin extends BaseActivity implements View.OnClickListener {

    private EditText id;//接收账号
    private EditText password;//接收密码
    private ImageView headPortrait;//头像

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
                Intent login = new Intent();//跳到新页面,还没写
                startActivity(login);
                break;
            case R.id.student_forget_password_button:
                Intent forgetPassword = new Intent();//跳到忘记密码
                startActivity(forgetPassword);
                break;
            case R.id.student_register_button:
                Intent rigister = new Intent(StudentLogin.this,Register.class);//跳到注册
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
}


