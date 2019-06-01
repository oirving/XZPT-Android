package com.djylrz.xzpt.activity.student;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class MyResumePreviewActivity extends AppCompatActivity {
    private TextView userName;//姓名
    private TextView basicInfo;//基本信息
    private ImageView headView;//头像
    private TextView expectPosition;//期望工作
    private TextView expectSalary;//薪水
    private TextView expectCity;//期望工作城市
    private TextView expectIndustry;//期望行业
    private TextView practice;//实习经历
    private TextView projects;//项目经历
    private TextView awards;//获奖证书
    private Button saveResume;//保存按钮
    private TextView school;//学校
    private TextView speciality;//专业
    private TextView time;//在校时间

    private User user = new User();//用户实体对象
    private String token;
    private RequestQueue requestQueue;
    private final String TAG = "MyResumePreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume_preview);
        userName = (TextView)findViewById(R.id.user_name);
        basicInfo = (TextView)findViewById(R.id.basic_info);
        expectPosition = (TextView)findViewById(R.id.job_name);
        expectSalary = (TextView)findViewById(R.id.salary);
        expectCity = (TextView)findViewById(R.id.job_location);
        expectIndustry = (TextView)findViewById(R.id.job_industry);
        school = (TextView)findViewById(R.id.school_name);
        speciality = (TextView)findViewById(R.id.speciality);
        time = (TextView)findViewById(R.id.education_time);
        practice = (TextView)findViewById(R.id.practice_textview);
        projects = (TextView)findViewById(R.id.project_textview);
        awards = (TextView)findViewById(R.id.awards_textview);
        saveResume = (Button)findViewById(R.id.save_resume);
        saveResume.setOnClickListener(new View.OnClickListener() {
            //todo 新建一份简历时，预览后点击保存就保存到已有简历列表 ->小榕
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"saveResume",Toast.LENGTH_SHORT).show();
            }
        });
        getStudentInfo();
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
                Log.d(TAG, "onCreate: 获取个人信息，只填了token"+new Gson().toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN+user.getToken(),new JSONObject(new Gson().toJson(user)),
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
                                    editor.putString("student", new Gson().toJson(user));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initPage(user);
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
    //todo 初始化界面 ->小榕
    public void initPage(User user/* Resume resume*/) {
        userName.setText(user.getUserName());
        basicInfo.setText(user.getPresentCity()+"-"+String.valueOf(user.getAge())+"-"+user.getHighestEducation());
        //expectPosition.setText(resume.getExpectWork());
        expectSalary.setText(user.getExpectSalary());
        // expectCity.setText(resume.getExpectedCity());
//        expectIndustry.setText(); todo 填入行业 ->小榕
//        practice.setText(resume.getPracticalExperience());
//        projects.setText(resume.getProjectExperience());
//        awards.setText(resume.getCertificate());
        school.setText(user.getSchool());
        speciality.setText(user.getSpecialty());
        time.setText(user.getStartTime()+"-"+user.getEndTime());
    }
}
