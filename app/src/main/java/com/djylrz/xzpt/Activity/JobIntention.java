package com.djylrz.xzpt.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.djylrz.xzpt.R;

public class JobIntention extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobIntention";

    private EditText job;
    private EditText location;
    private EditText companyDerection;
    private EditText salary;
    private TextView checkindate;

    private String  checkInYear;//记录入职的出生年
    private String checkInMonth;//月
    private String checkInDay;//日

    private String Job;
    private String Location;
    private String CompanyDerection;
    private String Salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_intention);

        Button back = (Button)findViewById(R.id.job_intention_finish_button);//取消按钮
        back.setOnClickListener(this);

        Button next = (Button)findViewById(R.id.info_complete_button);//完成按钮
        next.setOnClickListener(this);

        checkindate = (TextView)findViewById(R.id.show_checkin_date);

        job = (EditText)findViewById(R.id.info_job);
        Job = job.getText().toString();//期望职位

        location = (EditText)findViewById(R.id.info_location);
        Location = location.getText().toString();//工作地点

        companyDerection = (EditText)findViewById(R.id.info_company_derection);
        CompanyDerection = companyDerection.getText().toString();//企业方向

        salary = (EditText)findViewById(R.id.info_salary);
        Salary = salary.getText().toString();//薪资
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkin_date_button:
                //获取入职时间
                new DatePickerDialog(JobIntention.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        checkInYear=String.valueOf(year);//出生年
                        checkInMonth=String.valueOf(monthOfYear+1);//出生月
                        checkInDay=String.valueOf(dayOfMonth);//出生日
                        checkindate.setText("期望入职时间："+String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2019,4,20).show();
                break;

            case R.id.job_intention_finish_button:
                //取消按钮
                finish();
                break;
            case R.id.info_complete_button:
                //下一步按钮
                //todo：把当前获取的个人信息和上个界面的信息收集，完成个人信息填写——to小榕

                break;
            default:
                break;
        }
    }

}


