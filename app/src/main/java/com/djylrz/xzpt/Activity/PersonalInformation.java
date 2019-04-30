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

public class PersonalInformation extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PersonalInformation";

    private EditText name;//姓名
    private Spinner sex;//性别
    private EditText age;//年龄
    private EditText phoneNum;//电话号码
    private EditText mailAddress;//邮箱
    private EditText currentCity;//居住城市
    private EditText school;//毕业院校
    private Spinner highestEducation;//最高学历
    private EditText major;//主修专业
    private EditText startTime;//教育开始时间
    private EditText endTime;//教育结束时间
    private ArrayAdapter<String> sexAdapter;
    private ArrayAdapter<String> highestEducationAdapter;
    private String[] sexArray=new String[]{"男","女"};
    private String[] highestEducationArray=new String[]{"学历不限","大专","本科","硕士","博士及以上"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pernoal_information);

        name =  (EditText) findViewById(R.id.info_name);
        age = (EditText) findViewById(R.id.info_age);
        phoneNum = (EditText) findViewById(R.id.info_phonenum);
        mailAddress = (EditText) findViewById(R.id.info_mail);
        currentCity = (EditText) findViewById(R.id.info_currentcity);
        school = (EditText) findViewById(R.id.info_school);
        major = (EditText) findViewById(R.id.info_major);
        startTime = (EditText) findViewById(R.id.info_start_time);
        endTime = (EditText) findViewById(R.id.info_end_time);
        sex = (Spinner) findViewById(R.id.sex_spinner);
        highestEducation = (Spinner) findViewById(R.id.highestEducation);
        //性别下拉框
        sexAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sexArray);
        sexAdapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(sexAdapter);
        //性别下拉框点击事件
        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PersonalInformation.this,"性别"+sexArray[position], Toast.LENGTH_SHORT).show();
                //todo 获得position，映射为性别存入user->小榕
                //user.setSex(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //最高学历下拉框
        highestEducationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,highestEducationArray);
        highestEducationAdapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        highestEducation.setAdapter(highestEducationAdapter);
        //点击事件
        highestEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PersonalInformation.this,"性别"+highestEducationArray[position], Toast.LENGTH_SHORT).show();
                //todo 获得position，映射为学历存入user->小榕
                //user.setHighestEducation(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button next = (Button)findViewById(R.id.info_next_button);//保存按钮
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存按钮
            case R.id.info_next_button:
                //保存参数
                //todo 保存获得数据,确认保存状态 ->小榕
//                if() { //保存成功
                   finish();//结束当前页面
//                } else {
//                    Toast.makeText(PersonalInformation.this,"保存失败", Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                break;
        }
    }
    //初始化页面可用这个函数
//    public initpage(User user) {
//        name.setText(user.getUserName);
//        age.setText();
//        phoneNum.setText();
//        mailAddress.setText();
//        currentCity.setText();
//        school.setText();
//        major.setText();
//        startTime.setText();
//        endTime.setText();
//    }
}

