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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class RecruitmentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView jobName;
    private TextView salary;
    private TextView location;
    private TextView degree;
    private TextView workTime;
    private TextView companyName;
    private TextView description;
    private TextView deliveryRequest;
    private TextView industryLabel;
    private TextView stationLabel;
    private TextView contact;
    private Button delivery;
    private Button chat;

    private Recruitment recruitment;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detail);
        jobName = findViewById(R.id.jobName);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        degree = findViewById(R.id.degree);
        workTime = findViewById(R.id.workTime);
        companyName = findViewById(R.id.companyName);
        description = findViewById(R.id.description);
        deliveryRequest = findViewById(R.id.deliveryRequest);
        industryLabel = findViewById(R.id.industryLabel);
        stationLabel = findViewById(R.id.stationLabel);
        delivery = findViewById(R.id.delivery);
        chat = findViewById(R.id.chat);
        contact = findViewById(R.id.contact);

        delivery.setOnClickListener(this);
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
        //TODO  通过Intent获取招聘信息的id
        int recruitmentId = 400;
        SharedPreferences userToken = getSharedPreferences("token",0);
        String token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_USER_GET_RECRUITMENT +token+"&recruitmentId="+recruitmentId, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Type jsonType = new TypeToken<TempResponseData<Recruitment>>() {}.getType();
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
                            public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        TempResponseData<Recruitment> tempResponseData = gson.fromJson(response.toString(),jsonType);
                        recruitment = tempResponseData.getResultObject();
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
        jobName.setText(recruitment.getJobName());
        salary.setText(recruitment.getSalary());
        location.setText(recruitment.getLocation());
        degree.setText(recruitment.getDegree());
        workTime.setText(recruitment.getWorkTime()+"");
        companyName.setText(recruitment.getCompanyName());
        description.setText(recruitment.getDescription());
        deliveryRequest.setText(recruitment.getDeliveryRequest());
        industryLabel.setText(recruitment.getIndustryLabel()+" ");
        stationLabel.setText(recruitment.getStationLabel());
        contact.setText(recruitment.getContact());
    }
}
