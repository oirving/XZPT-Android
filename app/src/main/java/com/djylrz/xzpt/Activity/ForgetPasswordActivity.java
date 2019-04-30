package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djylrz.xzpt.R;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mailAddress;
    private Button findBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mailAddress = (EditText)findViewById(R.id.mail_address_edittext);//邮箱
        findBack = (Button) findViewById(R.id.find_back_button);//找回按钮
        findBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //todo 发送邮件-->小榕
            case R.id.find_back_button:
                //成功后的跳转
                Intent intent = new Intent(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                startActivity(intent);

        }
    }
}
