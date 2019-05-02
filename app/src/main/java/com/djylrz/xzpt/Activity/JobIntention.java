package com.djylrz.xzpt.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.djylrz.xzpt.R;


public class JobIntention extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobIntention";

    private EditText job;
    private EditText workCity;
    private EditText industry;
    private EditText basicSalary;
    private EditText topSalary;
    private Spinner workTime;
    private Button save;
    private ArrayAdapter<String> workTimeAdapter;
    private String[] workTimes = new String[] {"995","996","955"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_intention);
        job = (EditText)findViewById(R.id.info_job);
        workCity = (EditText) findViewById(R.id.info_location);
        industry = (EditText) findViewById(R.id.info_industry);
        basicSalary = (EditText) findViewById(R.id.info_basic_salary);
        topSalary = (EditText) findViewById(R.id.info_top_salary);
        workTime = (Spinner) findViewById(R.id.work_time_spinner);
        save = (Button) findViewById(R.id.info_next_button);
        save.setOnClickListener(this);
        initpage();//初始化页面信息
        workTimeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,workTimes);
        workTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workTime.setAdapter(workTimeAdapter);
        //性别下拉框点击事件
        workTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(JobIntention.this,"工作时间制度"+workTimes[position], Toast.LENGTH_SHORT).show();
                //todo 获得position，映射为时间制度存入user->小榕
                //user.setWorkTime(position+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_next_button:
                //todo 保存填入的数据 ->小榕
                //如果保存成功，则关掉本页面
                finish();
                Toast.makeText(JobIntention.this,"职位"+job.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    //初始化界面信息
    public void initpage() {
        job.setText("ahdfia");
        workCity.setText("ajdhf");
        industry.setText("asdn");
        basicSalary.setText("sadjf");
        topSalary.setText("hsuadifu");
        workTime.setVerticalScrollbarPosition(1);
    }

}


