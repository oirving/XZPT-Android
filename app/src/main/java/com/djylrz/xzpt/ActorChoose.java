package com.djylrz.xzpt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ActorChoose extends BaseActivity implements View.OnClickListener{

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
                Intent student = new Intent(ActorChoose.this,StudentLogin.class);
                startActivity(student);
                Toast.makeText(ActorChoose.this,"学生用户",Toast.LENGTH_SHORT).show();
                break;
            case R.id.choose_company:
                Intent company = new Intent(ActorChoose.this,CompanyLogin.class);
                startActivity(company);
                Toast.makeText(ActorChoose.this,"企业用户",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
