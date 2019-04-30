package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.djylrz.xzpt.R;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText verificationCode;
    private EditText password;
    private EditText password2;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        verificationCode = (EditText) findViewById(R.id.verification_code);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        confirm = (Button) findViewById(R.id.find_back_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //todo 重置密码校验和存入-->小榕
            case R.id.find_back_button:
                //成功后的跳转
                Intent intent = new Intent(ResetPasswordActivity.this,ActorChoose.class);
                startActivity(intent);

        }
    }
}
