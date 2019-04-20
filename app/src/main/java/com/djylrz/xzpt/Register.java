package com.djylrz.xzpt;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends BaseActivity implements View.OnClickListener {
    private EditText mail;//邮箱
    private EditText password;//密码
    private EditText passwordCheck;//确认密码

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
        String mail=this.mail.getText().toString();//邮件地址
        String password=this.password.getText().toString();//密码
        String passwordCheck=this.passwordCheck.getText().toString();

        if(mailChek(mail)&&passwordCheck(password,passwordCheck)) {
            //验证通过
            Intent intent = new Intent(Register.this,PersonalInformation.class);
            startActivity(intent);
            Toast.makeText(Register.this,"已发送邮件至:"+mail,Toast.LENGTH_LONG).show();
            //传到数据库...
            //发邮件...
        }
        else if(!mailChek(mail)) {
            Toast.makeText(Register.this,"邮箱错误:"+mail,Toast.LENGTH_LONG).show();
        }
        else if(!passwordCheck(password,passwordCheck)) {
            Toast.makeText(Register.this,"两次输入的密码不同或为空",Toast.LENGTH_LONG).show();
        }
    }

    public boolean mailChek(String mail) {
        //邮件格式检查
        if(mail.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
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
}
