package com.djylrz.xzpt.activityStudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.datePicker.CustomDatePicker;
import com.djylrz.xzpt.datePicker.DateFormatUtils;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ArrayAdapter<String> sexAdapter;
    private ArrayAdapter<String> highestEducationAdapter;
    private TextView startTime;
    private java.util.Date startDate;
    private java.util.Date endDate;
    private TextView endTime;
    private CustomDatePicker startTimeDatePicker,endTimeDatePicker;
    private String[] sexArray=new String[]{"默认","男","女"};
    private String[] highestEducationArray=new String[]{"学历不限","大专","本科","硕士","博士及以上"};

    private User user = new User();//用户实体对象
    private String token;
    private RequestQueue requestQueue;

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
        startTime = (TextView) findViewById(R.id.info_start_time);
        startTime.setOnClickListener(this);
        endTime = (TextView) findViewById(R.id.info_end_time);
        endTime.setOnClickListener(this);
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
                Log.d(TAG, "性别："+position);
                user.setSex(position);
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
                Log.d(TAG, "最高学历："+position);
                user.setHighestEducation(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button next = (Button)findViewById(R.id.info_next_button);//保存按钮
        next.setOnClickListener(this);
        initStartTimeDatePicker();
        initEndTimeDatePicker();
        getStudentInfo();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_start_time:
                // 日期格式为yyyy-MM-dd
                startTimeDatePicker.show(startTime.getText().toString());
                break;
            case R.id.info_end_time:
                // 日期格式为yyyy-MM-dd
                endTimeDatePicker.show(endTime.getText().toString());
                break;
            //保存按钮
            case R.id.info_next_button:
                if (isPhone(phoneNum.getText().toString())) {
                    //保存参数
                    user.setUserName(name.getText().toString());//名字
                    user.setSpecialty(major.getText().toString());//专业
                    user.setUserName(name.getText().toString());//名字
                    user.setAge(Integer.parseInt(age.getText().toString()));//年龄
                    user.setEmail(mailAddress.getText().toString());//邮件
                    user.setPresentCity(currentCity.getText().toString());//当前城市
                    user.setSchool(school.getText().toString());//学校
                    user.setTelephone(phoneNum.getText().toString());//电话，没有限定输入格式

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        startDate =  sdf.parse(startTime.getText().toString());
                        endDate =  sdf.parse(endTime.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate!=null){
                        calendar.setTime(startDate);
                        user.setStartTime(new java.sql.Date(calendar.getTime().getTime()));//教育开始时间
                    }
                    if (endDate!=null){
                        calendar.setTime(endDate);
                        user.setEndTime(new java.sql.Date(calendar.getTime().getTime()));//教育结束时间，string->Date,没有限定输入格式                ;
                    }

                    //发送修改个人信息请求
                    Log.d(TAG, "onClick: "+PostParameterName.POST_URL_UPDATE_USER_INRO+user.getToken());
                    try {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create();
                        Log.d(TAG, "onClick: "+new Gson().toJson(user));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_UPDATE_USER_INRO + user.getToken(), new JSONObject(gson.toJson(user)),
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
                                                        RxToast.success("修改个人信息成功");
                                                        getStudentInfo();
                                                        finish();//保存成功，结束当前页面
                                                    }break;
                                                    default:{
                                                        RxToast.error("修改个人信息失败");
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
                } else {
                    RxToast.warning("电话号码输入有误");
                }
                break;
            default:
                break;
        }
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    //初始化页面可用这个函数
    private void initpage(User user) {
        setEditTextSaveEnableFalse();
        name.setText(user.getUserName());
        age.setText(String.valueOf(user.getAge()));
        phoneNum.setText(user.getTelephone());
        mailAddress.setText(user.getEmail());
        currentCity.setText(user.getPresentCity());
        school.setText(user.getSchool());
        major.setText(user.getSpecialty());
        highestEducation.setSelection((int)user.getHighestEducation());
        sex.setSelection((int)user.getSex());
        Calendar calendar = Calendar.getInstance();
        if (user.getStartTime()!=null){
            calendar.setTime(user.getStartTime());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(calendar.getTime());
            startTime.setText(dateString);
        }else{
            startTime.setText("   ");
        }
        if (user.getEndTime()!=null){
            calendar.setTime(user.getEndTime());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(calendar.getTime());
            endTime.setText(dateString);
        }else{
            endTime.setText("   ");
        }
        Log.d(TAG, "initpage: -----");

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startTimeDatePicker.onDestroy();
        endTimeDatePicker.onDestroy();
    }

    private void initStartTimeDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        startTime.setText(DateFormatUtils.long2Str(beginTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        startTimeDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                startTime.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        startTimeDatePicker.setCancelable(true);
        // 不显示时和分
        startTimeDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        startTimeDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        startTimeDatePicker.setCanShowAnim(false);
    }

    private void initEndTimeDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = DateFormatUtils.str2Long("2040-05-01", false);

        endTime.setText(DateFormatUtils.long2Str(beginTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        endTimeDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                endTime.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        endTimeDatePicker.setCancelable(true);
        // 不显示时和分
        endTimeDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        endTimeDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        endTimeDatePicker.setCanShowAnim(false);
    }
}

