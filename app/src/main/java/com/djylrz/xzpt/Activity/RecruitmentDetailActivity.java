package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djylrz.xzpt.R;

public class RecruitmentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView job;
    private TextView workCity;
    private TextView highestEducation;
    private TextView basicSalary;
    private TextView topSalary;
    private TextView workTime;
    private TextView companyName;
    private TextView majorResponsebility;
    private TextView jobRecommand;
    private TextView workPlace;
    private TextView skills;
    private Button deliveryResume;
    private Button chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detail);
        job = (TextView) findViewById(R.id.job);
        workCity = (TextView) findViewById(R.id.work_city);
        highestEducation = (TextView) findViewById(R.id.highestEducation);
        basicSalary = (TextView) findViewById(R.id.basic_salary);
        topSalary =(TextView) findViewById(R.id.top_salary);
        workTime = (TextView) findViewById(R.id.work_time);
        companyName = (TextView) findViewById(R.id.company_name);
        majorResponsebility = (TextView) findViewById(R.id.major_responsebility);
        jobRecommand = (TextView) findViewById(R.id.job_recommend);
        workPlace = (TextView) findViewById(R.id.city_detail);
        skills = (TextView) findViewById(R.id.job);
        deliveryResume = (Button)findViewById(R.id.delivery);
        deliveryResume.setOnClickListener(this);
        chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delivery:
                //todo 保存公司状态，投递简历时能投到对应的公司 ->小榕
                Intent intent = new Intent(RecruitmentDetailActivity.this,MyResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                //暂时没有聊天功能
                finish();
                break;
        }
    }
    //todo 初始化本界面，把相应的信息填入 ->小榕
//    public void initPage() {
//        job.setText();
//        workCity.setText();
//        highestEducation.setText();
//        basicSalary.setText();
//        topSalary.setText();
//        workTime.setText();
//        companyName.setText();
//        majorResponsebility.setText();
//        jobRecommand.setText();
//        workPlace.setText();
//        skills.setText();
//    }
}
