package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.utils.Constants;

public class ResumeModelDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private TextView userSex;
    private TextView userPosition;

    private TextView awards;
    private TextView project;
    private TextView practice;
    private Button export;
    private Button edit;

    private Resume resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_model_details);

        Intent intent = getIntent();
        resume = (Resume) intent.getSerializableExtra("resume");//获取传递的简历信息


        userName=(TextView)findViewById(R.id.user_name);
        userSex=(TextView)findViewById(R.id.user_sex);
        userPosition=(TextView)findViewById(R.id.position);

        awards = (TextView) findViewById(R.id.awards);
        project = (TextView) findViewById(R.id.project_textview);
        practice = (TextView) findViewById(R.id.practice_textview);
        export = (Button) findViewById(R.id.export);
        export.setOnClickListener(this);
        edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(this);

        if (resume!=null){
            initPage(resume);
        }else{
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export:
                break;
            case R.id.edit:
                //todo : 跳转到编辑页面
                Intent intent = new Intent(ResumeModelDetailsActivity.this,NewResumeActivity.class);
                intent.putExtra("editResume",resume);
                startActivity(intent);
                finish();
                break;
                default:
                    break;
        }
    }

    private void initPage(Resume resume) {
        userName.setText(resume.getUserName());
        userSex.setText(Constants.SEX[(int)resume.getSex()]);
        userPosition.setText(resume.getExpectWork());
        awards.setText(resume.getCertificate());
        project.setText(resume.getProjectExperience());
        practice.setText(resume.getPracticalExperience());
        //简历没有年龄
    }
}
