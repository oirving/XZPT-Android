package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;

import org.w3c.dom.Text;

public class FeedbackAndSuggestion extends AppCompatActivity implements View.OnClickListener {

    private EditText feedback;//反馈文本
    private ImageView feedbackImage;//添加图片
    private TextView tips;//添加图片提示
    private EditText phone;//电话
    private Button submit;//提交按钮
    private String feedbacktext;//接收反馈文本文件
    private String phonetext;//接收电话

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_and_suggestion);

        //todo 反馈文本存入 ——to小榕
        feedback = (EditText) findViewById(R.id.feedback_text_edittext);
        feedbacktext = feedback.getText().toString();//反馈文本

        tips = (TextView) findViewById(R.id.feedback_tips);

        phone = (EditText) findViewById(R.id.phone_feedback_edittext);
        phonetext = phone.getText().toString();//联系方式

        feedbackImage = (ImageView) findViewById(R.id.feedback_picture_imageview);//反馈图片
        feedbackImage.setOnClickListener(this);

        submit = (Button) findViewById(R.id.submit_feedback_area);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_picture_imageview:
                //todo 反馈图片存入 ——to小榕 也不知道有没有这个接口
                tips.setVisibility(View.INVISIBLE);//点击图片后提示消失
                break;
            case R.id.submit_feedback_area:
                //todo 验证反馈内容是否成功 ——to小榕
                Intent intent = new Intent(FeedbackAndSuggestion.this,MyFeedback.class);//跳转到我的反馈页面
                startActivity(intent);
                Toast.makeText(FeedbackAndSuggestion.this,"反馈成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
