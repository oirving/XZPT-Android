package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.User;

public class NewResumeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewResumeActivity";
    private TextView name;//姓名
    private TextView sex;//性别
    private TextView age;//年龄
    private TextView phoneNum;//电话号码
    private TextView mailAddress;//邮箱
    private TextView currentCity;//居住城市
    private TextView school;//毕业院校
    private TextView highestEducation;//最高学历
    private TextView major;//主修专业
    private TextView startTime;//教育开始时间
    private TextView endTime;//教育结束时间
    private TextView job;//期望职位
    private TextView workCity;//工作城市
    private TextView industry;//行业
    private TextView workTime;//工作实践制度
    private TextView basicSalary;//起始工资
    private TextView topSalary;//
    private ImageView personalInfoEdit;
    private ImageView jobEdit;
    private ImageView awardsEdit;
    private ImageView projectEdit;
    private ImageView practiceEdit;
    private ImageView done;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_resume);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);//性别
        age = (TextView) findViewById(R.id.age);
        phoneNum = (TextView) findViewById(R.id.phonenum);
        mailAddress = (TextView) findViewById(R.id.mail);
        currentCity = (TextView) findViewById(R.id.currentCity);
        school = (TextView) findViewById(R.id.school);
        highestEducation =  (TextView) findViewById(R.id.highestEducation);
        major =  (TextView) findViewById(R.id.major);
        startTime =  (TextView) findViewById(R.id.start_time);
        endTime =  (TextView) findViewById(R.id.end_time);
        job =  (TextView) findViewById(R.id.position);
        workCity =  (TextView) findViewById(R.id.work_city);
        industry =  (TextView) findViewById(R.id.industry);
        workTime =  (TextView) findViewById(R.id.work_time);
        basicSalary =  (TextView) findViewById(R.id.basic_salary);
        topSalary =  (TextView) findViewById(R.id.top_salary);
        personalInfoEdit =  (ImageView) findViewById(R.id.edit_personalinfo);
        personalInfoEdit.setOnClickListener(this);
        jobEdit =  (ImageView) findViewById(R.id.edit_job);
        jobEdit.setOnClickListener(this);
        awardsEdit = (ImageView) findViewById(R.id.edit_awards);
        awardsEdit.setOnClickListener(this);
        projectEdit = (ImageView) findViewById(R.id.edit_project);
        projectEdit.setOnClickListener(this);
        practiceEdit = (ImageView) findViewById(R.id.edit_practice);
        practiceEdit.setOnClickListener(this);
        done = (ImageView) findViewById(R.id.done);
        done.setOnClickListener(this);
        //initPage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_personalinfo:
                //个人信息编辑
                Intent intent = new Intent(NewResumeActivity.this,PersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.edit_job:
                //工作期望编辑
                Intent intent1 = new Intent(NewResumeActivity.this,JobIntention.class);
                startActivity(intent1);
                break;
            case R.id.edit_awards:
                //荣誉证书编辑
                Intent intent2 = new Intent(NewResumeActivity.this,EditAwardsActivity.class);
                startActivity(intent2);
                break;
            case R.id.edit_project:
                //项目经历编辑
                Intent intent3 = new Intent(NewResumeActivity.this,EditProjectActivity.class);
                startActivity(intent3);
                break;
            case R.id.edit_practice:
                //实践经验编辑
                Intent intent4 = new Intent(NewResumeActivity.this,EditPracticeActivity.class);
                startActivity(intent4);
                break;
            case R.id.done:
                //todo 创建一份新的简历 并加入MyResumeActivity
                Intent intent5 = new Intent(NewResumeActivity.this,MyResumeActivity.class);
                startActivity(intent5);
                break;
                default:
                    break;
        }
    }
    //初始化页面
//    public void initPage() {
//        name.setText();
//        sex.setText();
//        age.setText();
//        phoneNum.setText();
//        mailAddress.setText();
//        currentCity.setText();
//        school.setText();
//        highestEducation.setText();
//        major.setText();
//        startTime.setText();
//        endTime.setText();
//        job.setText();
//        workCity.setText();
//        industry.setText();
//        workTime.setText();
//        basicSalary.setText();
//        topSalary.setText();
//    }
}
