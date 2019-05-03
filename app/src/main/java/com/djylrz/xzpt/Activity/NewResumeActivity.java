package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.Constants;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private TextView awardsTextView;
    private TextView projectTextView;
    private TextView practiceTextView;

    private User user = new User();
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_resume);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);//性别
        age = (TextView) findViewById(R.id.age);
        phoneNum = (TextView) findViewById(R.id.phonenum);
        mailAddress = (TextView) findViewById(R.id.mail);
        currentCity = (TextView) findViewById(R.id.current_city);
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

        awardsTextView=(TextView)findViewById(R.id.awards_textview);
        projectTextView=(TextView)findViewById(R.id.project_textview);
        practiceTextView=(TextView)findViewById(R.id.practice_textview);

        sharedPreferences = getSharedPreferences("user",0);
        initPage();

        Log.d(TAG, "onCreate: ");
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
                //todo 创建一份新的简历
                createResume();
                break;
                default:
                    break;
        }
    }
    //初始化页面
    public void initPage() {
        String userJson = sharedPreferences.getString("student",null);
        String awards = sharedPreferences.getString("award",null);
        String project = sharedPreferences.getString("project",null);
        String practice = sharedPreferences.getString("practice",null);
        if (userJson != null){
            user = new Gson().fromJson(userJson,User.class);
            name.setText(user.getUserName()!=null?user.getUserName():"");
            sex.setText(Constants.SEX[(int)user.getSex()]);
            age.setText(user.getAge()!=0?String.valueOf(user.getAge()):"");
            phoneNum.setText(user.getTelephone()!=null ? user.getTelephone():"");
            mailAddress.setText(user.getEmail()!=null?user.getEmail():"");
            currentCity.setText(user.getPresentCity()!=null?user.getPresentCity():"");
            school.setText(user.getSchool()!=null ? user.getSchool():"");
            highestEducation.setText(Constants.EDUCATION_LEVEL[(int)user.getHighestEducation()]);
            major.setText(user.getSpecialty()!=null ? user.getSpecialty():"");


            Calendar calendar = Calendar.getInstance();
            if (user.getStartTime()!=null){
                calendar.setTime(new Date(user.getStartTime().getTime()));
                startTime.setText(calendar.get(Calendar.YEAR));
            }else{
                startTime.setText("");
            }
            if (user.getEndTime()!=null){
                calendar.setTime(new Date(user.getEndTime().getTime()));
                endTime.setText(user.getEndTime().toString());
            }else{
                endTime.setText("");
            }



            job.setText(user.getStationLabel()!=null ? user.getStationLabel():"");
            workCity.setText(user.getExpectedCity()!=null ? user.getExpectedCity():"");
            industry.setText(Constants.INDUSTRY_LABEL[(int)user.getIndustryLabel()]);
            workTime.setText(Constants.WORK_TIME[(int)user.getWorkTime()]);

            if (user.getExpectSalary() != null){
                // 按指定模式在字符串查找ak-bk
                String pattern = "([1-9]\\d*)(k-)([1-9]\\d*)(k)";
                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher matcher = r.matcher(user.getExpectSalary());
                if (matcher.find()){
                    basicSalary.setText(matcher.group(1));
                    topSalary.setText(matcher.group(3));
                }

            }
        }
        awardsTextView.setText(awards!=null?awards:"");
        projectTextView.setText(project!=null?project:"");
        practiceTextView.setText(practice!=null?practice:"");

    }

    //创建新简历
    private void createResume(){
        //获取简历所需信息
        Resume newResume = new Resume();
        //User的信息
        newResume.setUserId(user.getUserId());
        newResume.setTelephone(user.getTelephone());
        newResume.setUserName(user.getUserName());
        newResume.setHeadUrl(user.getHeadUrl());
        newResume.setEmail(user.getEmail());
        newResume.setSex(user.getSex());
        newResume.setPresentCity(user.getPresentCity());
        newResume.setExpectedCity(user.getExpectedCity());
        newResume.setSchool(user.getSchool());
        newResume.setSpeciality(user.getSpecialty());
        newResume.setStartTime(user.getStartTime());
        newResume.setEndTime(user.getEndTime());
        newResume.setHighestEducation(user.getHighestEducation());
        //奖项的信息
        String awards = sharedPreferences.getString("award",null);
        if (awards!=null){
            newResume.setCertificate(awards);
        }
        //项目经历
        String project = sharedPreferences.getString("project",null);
        if (project!=null){
            newResume.setProjectExperience(project);
        }
        //实践经历
        String practice = sharedPreferences.getString("practice",null);
        if (practice!=null){
            newResume.setPracticalExperience(practice);
        }

        //网络处理
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());
        try {
            Log.d(TAG, "createResume: "+new Gson().toJson(newResume));
            Log.d(TAG, "createResume: URL is "+PostParameterName.POST_URL_CREATE_RESUME+user.getToken());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_CREATE_RESUME+user.getToken(),
                    new JSONObject(new Gson().toJson(newResume)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()){
                                        case "200":{
                                            //跳转到我的简历界面
                                            Toast.makeText(NewResumeActivity.this, "创建简历成功", Toast.LENGTH_SHORT).show();
                                            //todo 并打开MyResumeActivity
                                            Intent intent5 = new Intent(NewResumeActivity.this,MyResumeActivity.class);
                                            startActivity(intent5);
                                            finish();
                                        }break;
                                        default:{
                                            Toast.makeText(NewResumeActivity.this, "创建简历失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "创建简历失败"+postResult.getResultCode());
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }});
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: ");
        initPage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        initPage();
    }
}
