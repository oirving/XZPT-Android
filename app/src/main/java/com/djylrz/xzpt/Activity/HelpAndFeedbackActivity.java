package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;

public class HelpAndFeedbackActivity extends BaseActivity implements View.OnClickListener {

    private TextView version;//版本信息
    private Button suggestionAndFeedback;//反馈与建议按钮
    private Button myFeedback;//我的反馈按钮
    private Button updateCheck;//检查更新按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        version = (TextView) findViewById(R.id.version_textview);

        suggestionAndFeedback = (Button) findViewById(R.id.suggestion_button);
        suggestionAndFeedback.setOnClickListener(this);

        myFeedback = (Button) findViewById(R.id.my_feedback_button);
        myFeedback.setOnClickListener(this);

        updateCheck = (Button) findViewById(R.id.update_check_button);
        updateCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suggestion_button:
                Intent intent = new Intent(HelpAndFeedbackActivity.this,FeedbackAndSuggestion.class);
                startActivity(intent);
                break;
            case R.id.my_feedback_button:
                Intent intent1 = new Intent(HelpAndFeedbackActivity.this,MyFeedback.class);
                startActivity(intent1);
                break;
            case R.id.update_check_button:
                //todo 版本更新检查——to小榕
                String isUpdate="已是最新版本";
                Toast.makeText(HelpAndFeedbackActivity.this,isUpdate,Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }
}
