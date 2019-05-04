package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class RecruitmentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView job;
    private TextView workCity;
    private TextView highestEducation;
    private TextView basicSalary;
    private TextView topSalary;
    private TextView workTime;
    private TextView companyName;
    private TextView majorResponsebility;
    private TextView jobRecommand;
    private TextView workPlace;
    private TextView skills;
    private Button deliveryResume;
    private Button chat;

    private Recruitment recruitment;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detail);
        job = (TextView) findViewById(R.id.job);
        workCity = (TextView) findViewById(R.id.work_city);
        highestEducation = (TextView) findViewById(R.id.highestEducation);
        basicSalary = (TextView) findViewById(R.id.basic_salary);
        topSalary = (TextView) findViewById(R.id.top_salary);
        workTime = (TextView) findViewById(R.id.work_time);
        companyName = (TextView) findViewById(R.id.company_name);
        majorResponsebility = (TextView) findViewById(R.id.major_responsebility);
        jobRecommand = (TextView) findViewById(R.id.job_recommend);
        workPlace = (TextView) findViewById(R.id.city_detail);
        skills = (TextView) findViewById(R.id.job);
        deliveryResume = (Button) findViewById(R.id.delivery);
        deliveryResume.setOnClickListener(this);
        chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(this);

        /** 初始化请求队列 */
        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delivery:
                //todo 保存公司状态，投递简历时能投到对应的公司 ->小榕
                Intent intent = new Intent(RecruitmentDetailActivity.this, MyResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                //暂时没有聊天功能
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences userToken = getSharedPreferences("token",0);
        String token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_USER_GET_RECRUITMENT + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Type jsonType = new TypeToken<TempResponseData<Recruitment>>() {}.getType();
                        recruitment = new Gson().fromJson(response.toString(),jsonType);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initPage(recruitment);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    //todo 初始化本界面，把相应的信息填入 ->小榕
    public void initPage(Recruitment recruitment) {
        job.setText(recruitment.getJobName());
        workCity.setText(recruitment.getLocation());
        highestEducation.setText(recruitment.getDegree());
        basicSalary.setText(recruitment.getSalary());
        topSalary.setText(recruitment.getSalary());
        workTime.setText(recruitment.getWorkTime()+"");
        companyName.setText(recruitment.getCompanyName());
//        majorResponsebility.setText(recruitment.get);
//        jobRecommand.setText();
//        workPlace.setText();
//        skills.setText();
    }
}
