package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.Constants;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewResumeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewResumeActivity";
    private Toolbar toolbar;//标题栏
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

    private TextView editOrCreate;

    private Resume resumeToEdit;

    private boolean alreadyEdited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_resume);
        //获取布局控件
        toolbar = (Toolbar)findViewById(R.id.new_resume_toolbar);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);//性别
        age = (TextView) findViewById(R.id.age);
        phoneNum = (TextView) findViewById(R.id.phonenum);
        mailAddress = (TextView) findViewById(R.id.mail);
        currentCity = (TextView) findViewById(R.id.current_city);
        school = (TextView) findViewById(R.id.school);
        highestEducation = (TextView) findViewById(R.id.highestEducation);
        major = (TextView) findViewById(R.id.major);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        job = (TextView) findViewById(R.id.position);
        workCity = (TextView) findViewById(R.id.work_city);
        industry = (TextView) findViewById(R.id.industry);
        workTime = (TextView) findViewById(R.id.work_time);
        basicSalary = (TextView) findViewById(R.id.basic_salary);
        topSalary = (TextView) findViewById(R.id.top_salary);
        personalInfoEdit = (ImageView) findViewById(R.id.edit_personalinfo);
        personalInfoEdit.setOnClickListener(this);
        jobEdit = (ImageView) findViewById(R.id.edit_job);
        jobEdit.setOnClickListener(this);
        awardsEdit = (ImageView) findViewById(R.id.edit_awards);
        awardsEdit.setOnClickListener(this);
        projectEdit = (ImageView) findViewById(R.id.edit_project);
        projectEdit.setOnClickListener(this);
        practiceEdit = (ImageView) findViewById(R.id.edit_practice);
        practiceEdit.setOnClickListener(this);
//        done.setOnClickListener(this);

        awardsTextView = (TextView) findViewById(R.id.awards_textview);
        projectTextView = (TextView) findViewById(R.id.project_textview);
        practiceTextView = (TextView) findViewById(R.id.practice_textview);



        sharedPreferences = getSharedPreferences("user", 0);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.done_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_menu_done:
                        switch (toolbar.getTitle() + "") {
                        case "修改简历":
                            editResume();
                            break;
                        case "创建新简历":
                            //创建一份新的简历
                            createResume();
                            break;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        if ((resumeToEdit = (Resume) getIntent().getSerializableExtra("editResume")) != null) {
            toolbar.setTitle("修改简历");
//            editOrCreate.setText("修改简历");
            getResume(resumeToEdit.getResumeId());
        } else {
//            editOrCreate.setText("创建新简历");
            toolbar.setTitle("创建新简历");
            getStudenInfo();
        }

        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (alreadyEdited) {//更改过了
            getStudenInfo();//获取更新后的user信息，并更新页面
        } else {
            alreadyEdited = true;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_personalinfo:
                //个人信息编辑
                Intent intent = new Intent(NewResumeActivity.this, PersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.edit_job:
                //工作期望编辑
                Intent intent1 = new Intent(NewResumeActivity.this, JobIntention.class);
                startActivity(intent1);
                break;
            case R.id.edit_awards:
                //荣誉证书编辑
                Intent intent2 = new Intent(NewResumeActivity.this, EditAwardsActivity.class);
                startActivity(intent2);
                break;
            case R.id.edit_project:
                //项目经历编辑
                Intent intent3 = new Intent(NewResumeActivity.this, EditProjectActivity.class);
                startActivity(intent3);
                break;
            case R.id.edit_practice:
                //实践经验编辑
                Intent intent4 = new Intent(NewResumeActivity.this, EditPracticeActivity.class);
                startActivity(intent4);
                break;

            default:
                break;
        }
    }

    //初始化页面
    public void initPage() {
        //本地信息user，awards，project，practice

        //把获取到的信息展示出来
        String awards = sharedPreferences.getString("award", null);
        String project = sharedPreferences.getString("project", null);
        String practice = sharedPreferences.getString("practice", null);
        name.setText(user.getUserName() != null ? user.getUserName() : "");
        sex.setText(Constants.SEX[(int) user.getSex()]);
        age.setText(user.getAge() != 0 ? String.valueOf(user.getAge()) : "");
        phoneNum.setText(user.getTelephone() != null ? user.getTelephone() : "");
        mailAddress.setText(user.getEmail() != null ? user.getEmail() : "");
        currentCity.setText(user.getPresentCity() != null ? user.getPresentCity() : "");
        school.setText(user.getSchool() != null ? user.getSchool() : "");
        highestEducation.setText(Constants.EDUCATION_LEVEL[(int) user.getHighestEducation()]);
        major.setText(user.getSpecialty() != null ? user.getSpecialty() : "");

        Calendar calendar = Calendar.getInstance();
        if (user.getStartTime() != null) {
            calendar.setTime(new Date(user.getStartTime().getTime()));
            startTime.setText(calendar.get(Calendar.YEAR));
        } else {
            startTime.setText("");
        }
        if (user.getEndTime() != null) {
            calendar.setTime(new Date(user.getEndTime().getTime()));
            endTime.setText(user.getEndTime().toString());
        } else {
            endTime.setText("");
        }

        job.setText(user.getStationLabel() != null ? user.getStationLabel() : "");
        workCity.setText(user.getExpectedCity() != null ? user.getExpectedCity() : "");
        industry.setText(Constants.INDUSTRY_LABEL[(int) user.getIndustryLabel()]);
        workTime.setText(Constants.WORK_TIME[(int) user.getWorkTime()]);

        if (user.getExpectSalary() != null) {
            // 按指定模式在字符串查找ak-bk
            String pattern = "([1-9]\\d*)(k-)([1-9]\\d*)(k)";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher matcher = r.matcher(user.getExpectSalary());
            if (matcher.find()) {
                basicSalary.setText(matcher.group(1));
                topSalary.setText(matcher.group(3));
            }

        }
        awardsTextView.setText(awards != null ? awards : "");
        projectTextView.setText(project != null ? project : "");
        practiceTextView.setText(practice != null ? practice : "");
    }

    //创建新简历
    private void createResume() {
        //获取简历所需信息
        Resume newResume = new Resume();
        getCurrentDataForEditOrCreateResume(newResume);

        //网络处理
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());
        try {
            Log.d(TAG, "createResume: " + new Gson().toJson(newResume));
            Log.d(TAG, "createResume: URL is " + PostParameterName.POST_URL_CREATE_RESUME + user.getToken());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_CREATE_RESUME + user.getToken(),
                    new JSONObject(new Gson().toJson(newResume)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到我的简历界面
                                            Toast.makeText(NewResumeActivity.this, "创建简历成功", Toast.LENGTH_SHORT).show();
                                            //并打开MyResumeActivity
                                            Intent intent5 = new Intent(NewResumeActivity.this, MyResumeActivity.class);
                                            startActivity(intent5);
                                            finish();
                                        }
                                        break;
                                        default: {
                                            Toast.makeText(NewResumeActivity.this, "创建简历失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "创建简历失败" + postResult.getResultCode());
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //初始化编辑简历页面，根据已有简历初始化
    private void initEditResume(Resume resume) {
        //初始化用户信息
        user.setUserId(resume.getUserId());
        user.setTelephone(resume.getTelephone());
        user.setUserName(resume.getUserName());
        user.setHeadUrl(resume.getHeadUrl());
        user.setEmail(resume.getEmail());
        user.setSex(resume.getSex());
        user.setPresentCity(resume.getPresentCity());
        user.setExpectedCity(resume.getExpectedCity());
        user.setSchool(resume.getSchool());
        user.setSpecialty(resume.getSpeciality());
        user.setStartTime(resume.getStartTime());
        user.setEndTime(resume.getEndTime());
        user.setHighestEducation(resume.getHighestEducation());
        user.setStationLabel(resume.getExpectWork());

        //初始化奖项信息，项目信息，实践信息
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("award", resume.getCertificate());
        editor.putString("project", resume.getProjectExperience());
        editor.putString("practice", resume.getPracticalExperience());
        editor.commit();
    }

    private void editResume() {
        Log.d(TAG, "editResume: RESUME ID is " + resumeToEdit.getResumeId());
        //获取用户token
        String token = getSharedPreferences("token", 0).getString(PostParameterName.STUDENT_TOKEN, null);

        //todo 获取修改后的信息
        getCurrentDataForEditOrCreateResume(resumeToEdit);

        //网络处理
        try {
            Log.d(TAG, "createResume: edit Resume " + new Gson().toJson(resumeToEdit));
            Log.d(TAG, "createResume: edit Resume URL is " + PostParameterName.POST_URL_UPDATE_RESUME + token +
                    "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_UPDATE_RESUME + token +
                    "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId(),
                    new JSONObject(new Gson().toJson(resumeToEdit)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到我的简历界面，打开MyResumeActivity
                                            Toast.makeText(NewResumeActivity.this, "更新简历成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        break;
                                        default: {
                                            Toast.makeText(NewResumeActivity.this, "更新简历失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "更新简历失败" + postResult.getResultCode());
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCurrentDataForEditOrCreateResume(Resume targetResume) {
        //个人信息&求职意向
        targetResume.setUserId(user.getUserId());
        targetResume.setTelephone(user.getTelephone());
        targetResume.setUserName(user.getUserName());
        targetResume.setHeadUrl(user.getHeadUrl());
        targetResume.setEmail(user.getEmail());
        targetResume.setSex(user.getSex());
        targetResume.setPresentCity(user.getPresentCity());
        targetResume.setExpectedCity(user.getExpectedCity());
        targetResume.setSchool(user.getSchool());
        targetResume.setSpeciality(user.getSpecialty());
        targetResume.setStartTime(user.getStartTime());
        targetResume.setEndTime(user.getEndTime());
        targetResume.setHighestEducation(user.getHighestEducation());
        targetResume.setExpectWork(user.getStationLabel());

        //奖项
        String awards = sharedPreferences.getString("award", null);
        if (awards != null) {
            targetResume.setCertificate(awards);
        }
        //项目经历
        String project = sharedPreferences.getString("project", null);
        if (project != null) {
            targetResume.setProjectExperience(project);
        }
        //实践经历
        String practice = sharedPreferences.getString("practice", null);
        if (practice != null) {
            targetResume.setPracticalExperience(practice);
        }
    }

    private void getStudenInfo() {
        //用户已经登录，查询个人信息并显示
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
        SharedPreferences userToken = getSharedPreferences("token", 0);
        final String token = userToken.getString(PostParameterName.STUDENT_TOKEN, null);
        if (token != null) {
            Log.d(TAG, "onCreate: TOKEN is " + token);
            user.setToken(token);

            try {
                Log.d(TAG, "onCreate: 获取个人信息，只填了token" + new Gson().toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN + user.getToken(), new JSONObject(new Gson().toJson(user)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                Type jsonType = new TypeToken<TempResponseData<User>>() {
                                }.getType();
                                final TempResponseData<User> postResult = new Gson().fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                user = postResult.getResultObject();
                                user.setToken(token);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initPage();//初始化页面信息
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    //获取单个简历信息

    //展示单个简历信息

    //简历信息填充到几个编辑模块中

    //重新生成简历

    //提交简历更新


    /*获取单个简历信息*/
    private void getResume(long resumeID) {
        String token = getSharedPreferences("token", 0).getString(PostParameterName.STUDENT_TOKEN, null);

        //网络处理
        Log.d(TAG, "createResume: edit Resume URL is " + PostParameterName.POST_URL_GET_RESUME + token +
                "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_RESUME + token +
                "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId(),
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d(TAG, "onResponse: 返回" + response.toString());
                        try {
                            switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                case "200": {
                                    JSONObject resumeObject = response.getJSONObject("resultObject");

                                    GsonBuilder builder = new GsonBuilder();
                                    builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                            return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                        }
                                    });
                                    Gson gson = builder.create();

                                    //解析resume
                                    Type jsonType = new TypeToken<Resume>() {
                                    }.getType();
                                    final Resume resume = gson.fromJson(resumeObject.toString(), jsonType);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            resumeToEdit = resume;
                                            initEditResume(resume);
                                            initPage();
                                        }
                                    });

                                }
                                break;
                                default: {
                                    Toast.makeText(NewResumeActivity.this, "更新简历失败", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "更新简历失败" + response.getString(PostParameterName.RESPOND_RESULTCODE));
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());
        VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request

    }

}
