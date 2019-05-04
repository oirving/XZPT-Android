package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djylrz.xzpt.R;

public class ResumeModelDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView awards;
    private TextView project;
    private TextView practice;
    private Button export;
    private Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_model_details);
        awards = (TextView) findViewById(R.id.awards);
        project = (TextView) findViewById(R.id.project_textview);
        practice = (TextView) findViewById(R.id.practice_textview);
        initPage();
        export = (Button) findViewById(R.id.export);
        export.setOnClickListener(this);
        edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export:
                break;
            case R.id.edit:
                Intent intent = new Intent(ResumeModelDetailsActivity.this,NewResumeActivity.class);
                startActivity(intent);
                finish();
                break;
                default:
                    break;
        }
    }

    public void initPage() {
        awards.setText("12345678");
        project.setText("123431587");
        practice.setText("687134281");
    }
}
