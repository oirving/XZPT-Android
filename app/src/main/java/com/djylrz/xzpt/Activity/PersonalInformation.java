package com.djylrz.xzpt.Activity;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

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

    private User user = new User();//用户实体对象
    private String token;
    public RequestQueue requestQueue;

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
                user.setSex(position+1);
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
                user.setHighestEducation(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button next = (Button)findViewById(R.id.info_next_button);//保存按钮
        next.setOnClickListener(this);


        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token",0);
        token = userToken.getString(PostParameterName.TOKEN,null);
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

                                final TempResponseData<User> postResult = new Gson().fromJson(response.toString(), TempResponseData.class);
//                                user = new Gson().from
//                                user = new Gson().fromJson(response.toString(),User.class);
                                Log.d(TAG, "onResponse: "+postResult.getResultCode());
//                                user = new Gson().fromJson(postResult.getResultObject(),User.class);
                                user = postResult.getResultObject();
                                user.setToken(token);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //todo:获取信息显示在编辑框上
                                        initpage(user);
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
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存按钮
            case R.id.info_next_button:
                //保存参数
                user.setUserName(name.getText().toString());//名字
                user.setSpecialty(major.getText().toString());//专业
                user.setUserName(name.getText().toString());//名字
                user.setAge(Integer.parseInt(age.getText().toString()));//年龄
                user.setEmail(mailAddress.getText().toString());//邮件
                user.setPresentCity(currentCity.getText().toString());//当前城市
                user.setSchool(school.getText().toString());//学校

                //todo：格式校验——>欧文
                user.setTelephone(phoneNum.getText().toString());//电话，没有限定输入格式
                //user.setStartTime(new Date(startTime.getText().toString()));//教育开始时间 string->Date，没有限定输入格式
                //user.setEndTime(endTime.getText().toString())//教育结束时间，string->Date,没有限定输入格式                ;
                //发送修改个人信息请求
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
                                                    Toast.makeText(PersonalInformation.this, "修改个人信息成功", Toast.LENGTH_SHORT).show();
                                                    finish();//保存成功，结束当前页面
                                                }break;
                                                default:{
                                                    Toast.makeText(PersonalInformation.this, "修改个人信息失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            PersonalInformation.this.finish();
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
                break;

            default:
                break;
        }
    }
    //初始化页面可用这个函数
    //todo:初始化界面
    private void initpage(User user) {
        setEditTextSaveEnableFalse();
        name.setText(user.getUserName());
        age.setText(String.valueOf(user.getAge()));
        phoneNum.setText(user.getTelephone());
        mailAddress.setText(user.getEmail());
        currentCity.setText(user.getPresentCity());
        school.setText(user.getSchool());
        major.setText(user.getSpecialty());
        Log.d(TAG, "initpage: -----");
        //startTime.setText(user.getStartTime().toString());
        //endTime.setText(user.getEndTime().toString());

    }

    private void setEditTextSaveEnableFalse(){
        name.setSaveEnabled(true);
        age.setSaveEnabled(false);
        phoneNum.setSaveEnabled(false);
        mailAddress.setSaveEnabled(false);
        currentCity.setSaveEnabled(false);
        school.setSaveEnabled(false);
        major.setSaveEnabled(false);
    }

}

