package com.djylrz.xzpt.activityStudent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.Constants;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JobIntentionActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "JobIntentionActivity";

    private EditText job;
    private EditText workCity;
    private Spinner industry;
    private EditText basicSalary;
    private EditText topSalary;
    private Spinner workTime;
    private Button save;
    private ArrayAdapter<String> workTimeAdapter;
    private ArrayAdapter<String> industryLabelAdapter;
    private User user = new User();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_intention);
        job = (EditText)findViewById(R.id.info_job);
        workCity = (EditText) findViewById(R.id.info_location);
        industry = (Spinner) findViewById(R.id.info_industry);
        basicSalary = (EditText) findViewById(R.id.info_basic_salary);
        topSalary = (EditText) findViewById(R.id.info_top_salary);
        workTime = (Spinner) findViewById(R.id.work_time_spinner);
        save = (Button) findViewById(R.id.info_next_button);
        save.setOnClickListener(this);

        //行业标签
        industryLabelAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Constants.INDUSTRY_LABEL);
        industryLabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        industry.setAdapter(industryLabelAdapter);
        //行业标签下拉框点击事件
        industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(JobIntentionActivity.this,"行业标签"+Constants.INDUSTRY_LABEL[position], Toast.LENGTH_SHORT).show();
                user.setIndustryLabel(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //工作时间
        workTimeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Constants.WORK_TIME);
        workTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workTime.setAdapter(workTimeAdapter);
        //工作时间下拉框点击事件
        workTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(JobIntentionActivity.this,"工作时间制度"+Constants.WORK_TIME[position], Toast.LENGTH_SHORT).show();
                user.setWorkTime(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getStudenInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_next_button:
                VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue

                //保存参数
                user.setStationLabel(job.getText().toString());//期待职业
                user.setExpectedCity(workCity.getText().toString());//工作地点
                user.setExpectSalary(basicSalary.getText().toString()+"k-"+topSalary.getText().toString()+"k");
                //发送修改求职意向请求
                Log.d(TAG, "onClick: "+PostParameterName.POST_URL_UPDATE_USER_INRO+user.getToken());
                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_UPDATE_USER_INRO+user.getToken(),new JSONObject(new Gson().toJson(user)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    final PostResult postResult = new Gson().fromJson(response.toString(),PostResult.class);
                                    Log.d(TAG, "onResponse: 修改个人信息"+response.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch(postResult.getResultCode()){
                                                case "200":{
                                                    Toast.makeText(JobIntentionActivity.this, "修改个人信息成功", Toast.LENGTH_SHORT).show();
                                                    getStudenInfo();
                                                    finish();//保存成功，结束当前页面
                                                }break;
                                                default:{
                                                    Toast.makeText(JobIntentionActivity.this, "修改个人信息失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            JobIntentionActivity.this.finish();
                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }});
                    VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void getStudenInfo(){
        //用户已经登录，查询个人信息并显示
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
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

                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

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
                                        initpage(user);//初始化页面信息
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }});
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    //初始化界面信息
    public void initpage(User user) {
        Log.d(TAG, "onCreate: ");
        job.setText(user.getStationLabel());
        workCity.setText(user.getExpectedCity());
        industry.setSelection((int)user.getIndustryLabel());

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


        workTime.setSelection((int)user.getWorkTime());
    }

}


