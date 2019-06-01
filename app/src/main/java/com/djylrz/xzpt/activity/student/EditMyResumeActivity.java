package com.djylrz.xzpt.activity.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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

import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class EditMyResumeActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "EditMyResumeActivity";
    private TextView userName;//姓名
    private TextView basicInfoPhone;//电话号码
    private TextView basicInfoEmail;//邮件
    private TextView basicInfoSex;//性别
    private ImageView edit;//名字旁的编辑
    private ImageView headView;//头像
    private TextView expectPosition;//期望工作
    private TextView expectSalary;//薪水
    private TextView expectCity;//期望工作城市
    //private TextView expectIndustry;//期望行业
    private TextView highestEducation;//最高学历
    private ImageView jobIntentionEdit;//前往工作意向编辑
    private ImageView practiceEdit;//前往实习经历编辑
    private ImageView projectEdit;//前往项目经历编辑
    private TextView school;//学校
    private TextView speciality;//专业
    private TextView time;//在校时间
    private ImageView educationEdit;//前往教育经历编辑
    private ImageView awardsEdit;//前往荣誉证书编辑
    private Button save;//编辑保存或者新建简历
    private Toolbar toolbar;//标题栏

    private TextView awardsTextView;
    private TextView projectTextView;
    private TextView practiceTextView;


    private User user = new User();//用户实体对象
    private String token;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    private Resume resumeToEdit;
    private boolean alreadyEdited = false;

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_resume);
        //获取布局控件
        headView = (ImageView)findViewById(R.id.head_image);
        userName = (TextView)findViewById(R.id.user_name);
        edit = (ImageView)findViewById(R.id.edit_imageview);
        edit.setOnClickListener(this);
        basicInfoPhone = (TextView)findViewById(R.id.basic_info_phone);
        basicInfoEmail = (TextView)findViewById(R.id.basic_info_email);//邮件
        basicInfoSex = (TextView)findViewById(R.id.basic_info_sex);//性别

        expectPosition = (TextView)findViewById(R.id.job_name);
        //expectSalary = (TextView)findViewById(R.id.salary);
        expectCity = (TextView)findViewById(R.id.job_location);
        //expectIndustry = (TextView)findViewById(R.id.job_industry);
        highestEducation = (TextView)findViewById(R.id.highest_education);
        jobIntentionEdit = (ImageView)findViewById(R.id.job_intention_next);
        jobIntentionEdit.setOnClickListener(this);
        practiceEdit = (ImageView)findViewById(R.id.practice_next);
        practiceEdit.setOnClickListener(this);
        projectEdit = (ImageView)findViewById(R.id.project_next);
        projectEdit.setOnClickListener(this);
        school = (TextView)findViewById(R.id.school_name);
        speciality = (TextView)findViewById(R.id.speciality);
        time = (TextView)findViewById(R.id.education_time);
        educationEdit = (ImageView)findViewById(R.id.education_next);
        educationEdit.setOnClickListener(this);
        awardsEdit = (ImageView)findViewById(R.id.awards_next);
        awardsEdit.setOnClickListener(this);
        save = (Button)findViewById(R.id.edit_or_create_save);
        save.setOnClickListener(this);

        awardsTextView = (TextView) findViewById(R.id.awards_text_view);
        projectTextView = (TextView) findViewById(R.id.project_text_view);
        practiceTextView = (TextView) findViewById(R.id.practice_text_view);



        sharedPreferences = getSharedPreferences("user",0);
        //设置标题栏和最底下的按钮
        toolbar = (Toolbar) findViewById(R.id.creat_or_edit_resume_toolbar);
        toolbar.setTitle(getIntent().getStringExtra(Constants.INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setText(Constants.EDIT_OR_CREATE_RESUME_SAVE_BUTTON);

        switch (getIntent().getStringExtra(Constants.INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME)){
            case Constants.EDIT_RESUME:
                resumeToEdit=(Resume)getIntent().getSerializableExtra("editResume");
                getResume();
//                initEditResume(resumeToEdit);
//                initPage();
                break;
            case Constants.CREATE_RESUME:
                getStudentInfo();
                break;
            default:
                break;
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (alreadyEdited) {//更改过了
            getStudentInfo();//获取更新后的user信息，并更新页面
        } else {
            alreadyEdited = true;
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //预览/下载
            case R.id.edit_or_create_save:
                switch (toolbar.getTitle() + "") {
                    case Constants.EDIT_RESUME:
                        editResume();
                        break;
                    case Constants.CREATE_RESUME:
                        createResume();
                        break;
                    default:
                        break;
                }
                break;
                //跳转到个人信息进行编辑
            case R.id.edit_imageview:
                intent = new Intent(EditMyResumeActivity.this,PersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.job_intention_next:
                intent = new Intent(EditMyResumeActivity.this,JobIntentionActivity.class);
                startActivity(intent);
                break;
            case R.id.practice_next:
                intent = new Intent(EditMyResumeActivity.this,EditPracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.project_next:
                intent = new Intent(EditMyResumeActivity.this,EditProjectActivity.class);
                startActivity(intent);
                break;
            case R.id.education_next:
                intent = new Intent(EditMyResumeActivity.this,PersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.awards_next:
                intent = new Intent(EditMyResumeActivity.this,EditAwardsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void getStudentInfo(){
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token",0);
        token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        if (token != null){
            Log.d(TAG, "onCreate: TOKEN is "+token);

            user.setToken(token);

            try {
                Log.d(TAG, "onCreate: 获取个人信息，只填了token"+gson.toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN+user.getToken(),new JSONObject(gson.toJson(user)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回"+response.toString());
                                Type jsonType = new TypeToken<TempResponseData<User>>() {}.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<User> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: "+postResult.getResultCode());
                                user = postResult.getResultObject();
                                user.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：："+response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("student", gson.toJson(user));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initPage();
                                        Log.d(TAG, "run: ------");
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }});
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    //初始化界面
    private void initPage() {
        //把获取到的信息展示出来
        String awards = sharedPreferences.getString("award", null);
        String project = sharedPreferences.getString("project", null);
        String practice = sharedPreferences.getString("practice", null);

        Uri imageUri = Uri.parse(PostParameterName.DOWNLOAD_URL_RESUME_IMAGE_PREFIX + user.getHeadUrl());
        Glide.with(getApplicationContext()).load(imageUri).into(headView);
        userName.setText(user.getUserName());
        basicInfoPhone.setText(user.getTelephone());

        basicInfoEmail.setText(user.getEmail());//邮件
        basicInfoSex.setText(Constants.SEX[(int)user.getSex()]);//性别
        expectPosition.setText(user.getStationLabel());
        //expectSalary.setText(user.getExpectSalary());
        expectCity.setText(user.getExpectedCity());
        //expectIndustry.setText(Constants.INDUSTRY_LABEL[(int) user.getIndustryLabel()]);
        highestEducation.setText(Constants.EDUCATION_LEVEL[(int)user.getHighestEducation()]);
        school.setText(user.getSchool());
        speciality.setText(user.getSpecialty());
        time.setText((user.getStartTime()+"    "+user.getEndTime()));

        awardsTextView.setText(awards != null ? awards : "");
        projectTextView.setText(project != null ? project : "");
        practiceTextView.setText(practice != null ? practice : "");

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


    /*编辑简历部分函数*///获取单个简历，存储到本地，本地展示，获取本地信息，上传服务器保存
    //获取单个简历信息
    private void getResume() {
        String token = getSharedPreferences("token", 0).getString(PostParameterName.STUDENT_TOKEN, null);

        //网络处理
        Log.d(TAG, "getResume:Resume URL is " + PostParameterName.POST_URL_GET_RESUME + token +
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
                                    builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                                    builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                        @Override
                                        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                            return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                        }
                                    });

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
                                    RxToast.error("获取简历失败");
                                    Log.d(TAG, "获取简历失败" + response.getString(PostParameterName.RESPOND_RESULTCODE));
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

        //获取修改后的信息
        getCurrentDataForEditOrCreateResume(resumeToEdit);


        //网络处理
        try {
            Log.d(TAG, "editResume: edit Resume " + gson.toJson(resumeToEdit));
            Log.d(TAG, "editResume: edit Resume URL is " + PostParameterName.POST_URL_UPDATE_RESUME + token +
                    "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_UPDATE_RESUME + token +
                    "&" + PostParameterName.REQUEST_RESUME_ID + "=" + resumeToEdit.getResumeId(),
                    new JSONObject(gson.toJson(resumeToEdit)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = gson.fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到我的简历界面，打开MyResumeActivity
                                            RxToast.success("更新简历成功");
                                            finish();
                                        }
                                        break;
                                        default: {
                                            RxToast.error("更新简历失败");
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

    /*创建新简历部分函数*/
    //创建新简历
    private void createResume() {
        //获取简历所需信息
        Resume newResume = new Resume();
        getCurrentDataForEditOrCreateResume(newResume);

        //网络处理
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());
        try {
            Log.d(TAG, "createResume: " + gson.toJson(newResume));
            Log.d(TAG, "createResume: URL is " + PostParameterName.POST_URL_CREATE_RESUME + user.getToken());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_CREATE_RESUME + user.getToken(),
                    new JSONObject(gson.toJson(newResume)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = gson.fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到我的简历界面
                                            RxToast.success("创建简历成功");
//                                            //并打开MyResumeActivity
//                                            Intent intent5 = new Intent(EditMyResumeActivity.this, MyResumeActivity.class);
//                                            startActivity(intent5);
                                            finish();
                                        }
                                        break;
                                        default: {
                                            RxToast.error("创建简历失败");
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

}
