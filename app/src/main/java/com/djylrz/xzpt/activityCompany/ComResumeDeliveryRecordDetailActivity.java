package com.djylrz.xzpt.activityCompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
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
import com.bumptech.glide.Glide;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.DefaultMessagesActivity;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.bean.ResumeDelivery;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.vondear.rxtool.RxTextTool;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;

public class ComResumeDeliveryRecordDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;//标题栏
    private TextView mTvAboutSpannable;//RXTextView布局
    private ComResumeDeliveryRecordDetailActivity mContext;//上下文context
    private RequestQueue requestQueue;//请求队列
    private ResumeDelivery resumeDeliveryRecordVO;//用于接收intent传过来的记录对象
    private RxDialogLoading rxDialogLoading;//加载动画对话框
    private Resume resume;//用于接收json的简历对象
    private Button btnRefuse;//拒绝按钮
    private Button btnNext;//下一步按钮
    private Button btnChat;//发起聊天按钮
    private BoomMenuButton bmbNext;//弹出式选择器
    private String strNext[]={"面试待安排","进入一面","进入二面","进入终面","通过面试"};//弹出式菜单文字
    private int nextNum = 0;//用户点击哪一个next菜单
    private static final String TAG = "ComResumeDeliveryRecord";
    private ResumeDelivery resumeDelivery;//用于发送json的简历投递记录对象
    private int colorArray[]={
            R.color.course_color6,
            R.color.course_color7,
            R.color.course_color8,
            R.color.course_color1,
            R.color.course_color2,
            R.color.course_color3,
            R.color.course_color4,
            R.color.course_color5,
            R.color.course_color9,
            R.color.course_color10,
            R.color.course_color11,};

    //新布局
    private TextView userName;//姓名
    private TextView basicInfoPhone;//电话号码
    private TextView basicInfoEmail;//邮件
    private TextView basicInfoSex;//性别
    private ImageView headView;//头像
    private TextView resumeRecordState;//投递状态
    private TextView expectPosition;//期望工作
    private TextView expectCity;//期望工作城市

    private TextView highestEducationTextView;//最高学历
    private TextView school;//学校
    private TextView speciality;//专业
    private TextView time;//在校时间

    private TextView awardsTextView;
    private TextView projectTextView;
    private TextView practiceTextView;

    private void getView(){
        userName = findViewById(R.id.user_name);//姓名
        basicInfoPhone = findViewById(R.id.basic_info_phone);//电话号码
        basicInfoEmail = findViewById(R.id.basic_info_email);//邮件
        basicInfoSex = findViewById(R.id.basic_info_sex);//性别
        headView = findViewById(R.id.head_image);//头像
        resumeRecordState = findViewById(R.id.resume_record_state);//简历投递状态

        expectPosition = findViewById(R.id.job_name);//期望工作
        expectCity = findViewById(R.id.job_location);//期望工作城市

        highestEducationTextView = findViewById(R.id.highest_education);//最高学历
        school = findViewById(R.id.school_name);//学校
        speciality = findViewById(R.id.speciality);//专业
        time = findViewById(R.id.education_time);//在校时间

        awardsTextView = findViewById(R.id.awards_text_view);
        projectTextView = findViewById(R.id.project_text_view);
        practiceTextView = findViewById(R.id.practice_text_view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_delivery_record_detial);
        //获取布局控件
        getView();
        toolbar = (Toolbar)findViewById(R.id.asa_toolbar);
        mTvAboutSpannable = (TextView)findViewById(R.id.tv_about_spannable);
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        btnRefuse = (Button)findViewById(R.id.btn_refuse);
        btnNext = (Button)findViewById(R.id.btn_next);
        btnChat = (Button)findViewById(R.id.btn_message);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("简历详情");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置加载动画
        rxDialogLoading = new RxDialogLoading(this);
        rxDialogLoading.setLoadingText("数据加载中");
        rxDialogLoading.setLoadingColor(R.color.colorPrimary);

        //设置按钮
        btnRefuse.setText("拒绝");
        btnRefuse.setOnClickListener(this);
        btnNext.setText("下一步");
        btnNext.setOnClickListener(this);
        btnChat.setText("发起聊天");
        btnChat.setOnClickListener(this);
        //设置弹出式选择器
        bmbNext = (BoomMenuButton) findViewById(R.id.bmb_next);
        bmbNext.setDimColor(0);
        bmbNext.setButtonEnum(ButtonEnum.Ham);//设置弹出样式
        bmbNext.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        bmbNext.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
        for (int i = 0; i < bmbNext.getButtonPlaceEnum().buttonNumber(); i++) {
            bmbNext.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.done)
                    .imagePadding(new Rect(10,10,10,10))
                    .normalText(strNext[i]).textSize(26).textGravity(Gravity.CENTER_VERTICAL)
                    .normalColorRes(colorArray[i+4])
                    .highlightedColorRes(colorArray[(i+4+1)])
            .shadowColor(Color.parseColor("#000000")));

        }
        bmbNext.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
//                tv_importance.setText(str_importance[index]);
                nextNum = index + 3;
                resumeDelivery.setDeliveryStatus(nextNum);
                //向服务器发起请求，修改简历状态
                modifyResumeDeliveryRecord();
            }
            @Override
            public void onBackgroundClick() { }
            @Override
            public void onBoomWillHide() { }
            @Override
            public void onBoomDidHide() { }
            @Override
            public void onBoomWillShow() { }
            @Override
            public void onBoomDidShow() { }
        });

        initView();
        resumeDelivery = new ResumeDelivery();
        //若简历为未查看状态，点入详情页后修改状态为2已查看
        if(resumeDeliveryRecordVO.getDeliveryStatus()==1){
            resumeDelivery.setResumeDeliveryId(resumeDeliveryRecordVO.getResumeDeliveryId());
            resumeDelivery.setDeliveryStatus(2);
            modifyResumeDeliveryRecord();
        }
        mContext = this;
        RxTool.init(this);

    }

    private void initView() {

        //获取传递过来的岗位信息
        Intent intent = getIntent();
        resumeDeliveryRecordVO = (ResumeDelivery) intent.getSerializableExtra("resumeDeliveryRecordVO");
        //根据resumeId获取简历详细信息
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);
        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_GET_RESUME_BY_ID + token + "&resumeId=" + resumeDeliveryRecordVO.getResumeId();
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(resumeDeliveryRecordVO)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            GsonBuilder builder = new GsonBuilder();
//                            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                                @Override
//                                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                                }
//                            });
//                            Gson gson =builder.create();
                            builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                    return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                }
                            });
                            Gson gson = builder
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type jsonType = new TypeToken<TempResponseData<Resume>>() {}.getType();
                            final TempResponseData<Resume> postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            resume = postResult.getResultObject();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //设置文本数据
                                    setText();
                                    //关闭加载动画
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    RxToast.error("获取数据失败，请重试");
                    finish();
                }});

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            RxToast.error("获取数据失败，请重试");
            finish();
            e.printStackTrace();
        }
    }
    public void setText(){
        String sex;//性别
        String highestEducation;//最高学历
        String resumeRecordType;//简历状态

        switch ((int) resume.getSex()){
            case 0:
                sex = "未填写";
                break;
            case 1:
                sex = "男";
                break;
            case 2:
                sex = "女";
                break;
            default:
                sex = "未知";
                break;
        }
        //1为未填写，2为大专及以上,3为本科及以上,4为硕士及以上,5为博士及以上
        switch ((int)resume.getHighestEducation()){
            case 1:
                highestEducation = "其他";
                break;
            case 2:
                highestEducation = "大专";
                break;
            case 3:
                highestEducation = "本科";
                break;
            case 4:
                highestEducation = "硕士";
                break;
            case 5:
                highestEducation = "博士";
                break;
            default:
                highestEducation = "未填写";
                break;
        }
        switch ((int)resumeDeliveryRecordVO.getDeliveryStatus()){
            case -1:
                resumeRecordType = "已拒绝";
                break;
            case 1:
                resumeRecordType = "未查看";
                break;
            case 2:
                resumeRecordType = "已查看";
                break;
            case 3:
                resumeRecordType = "面试待安排";
                break;
            case 4:
                resumeRecordType = "一面";
                break;
            case 5:
                resumeRecordType = "二面";
                break;
            case 6:
                resumeRecordType = "终面";
                break;
            case 7:
                resumeRecordType = "已录用";
                break;
            default:
                resumeRecordType = "未审核";
                break;
        }
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转到详情页
                Intent intent = new Intent(mContext, ComRecruitmentDetailActivity.class);
                intent.putExtra("recruitmentId",resumeDeliveryRecordVO.getRecruitmentId());
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };

        // 响应点击事件的话必须设置以下属性
        mTvAboutSpannable.setMovementMethod(LinkMovementMethod.getInstance());

        RxTextTool.getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
                .append(resumeDeliveryRecordVO.getUserName() + "\n").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.colorPrimary))
                .append("求职意向："+resumeDeliveryRecordVO.getRecruitmentName() + "\n\n").setBold().setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER)
                .setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8).setClickSpan(clickableSpan)
                .append("状态：" + resumeRecordType +"\n")
                .setBold().setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .setBackgroundColor(getResources().getColor(R.color.lightblue))
                .setForegroundColor(getResources().getColor(R.color.black))


                .append("基本信息" + "\n").setBold()
                .append("性    别：" + sex + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("毕业学校：" + resume.getSchool() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("专业名称：" + resume.getSpeciality() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("电子邮件：" + resume.getEmail() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("联系电话：" + resume.getTelephone() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("当前城市：" + resume.getPresentCity() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("期望城市：" + resume.getExpectedCity() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("最高学历：" + highestEducation + "\n\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)

                .append("荣誉证书" + "\n").setBold()
                .append(resume.getCertificate() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)

                .append("项目经历" + "\n").setBold()
                .append(resume.getProjectExperience() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)

                .append("实践经历" + "\n").setBold()
                .append(resume.getPracticalExperience() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)

                .into(mTvAboutSpannable);
        //简历数据填充到新布局中
        userName.setText(resume.getUserName());//姓名
        basicInfoPhone.setText(resume.getTelephone());//电话号码
        basicInfoEmail.setText(resume.getEmail());//邮件
        basicInfoSex.setText(sex);//性别
        resumeRecordState.setBackgroundColor(getResources().getColor(colorArray[(int)resume.getResumeStatus()+1]));
        resumeRecordState.setText(resumeRecordType);
        //resumeRecordType;//todo 简历状态
        Uri imgUri = Uri.parse(PostParameterName.DOWNLOAD_URL_RESUME_IMAGE_PREFIX + resume.getHeadUrl());//头像
        Glide.with(getApplicationContext()).load(imgUri).into(headView);

        expectPosition.setText((resume.getExpectWork()+""));//期望工作
        expectCity.setText((resume.getExpectedCity()+""));//期望工作城市

        highestEducationTextView.setText(highestEducation);//最高学历
        school.setText((resume.getSchool()+""));//学校
        speciality.setText((resume.getSpeciality()+""));//专业
        time.setText((resume.getStartTime()+"-"+resume.getEndTime()));//在校时间

        awardsTextView.setText((resume.getCertificate()+""));
        projectTextView.setText((resume.getProjectExperience()+""));
        practiceTextView.setText((resume.getPracticalExperience()+""));
    }


    @Override
    public void onClick(View v) {
        resumeDelivery.setResumeDeliveryId(resumeDeliveryRecordVO.getResumeDeliveryId());
        switch (v.getId()){
            case R.id.btn_refuse:
                resumeDelivery.setDeliveryStatus(-1);
                modifyResumeDeliveryRecord();
                break;
            case R.id.btn_next:
                bmbNext.boom();
                break;
            case R.id.btn_message:
                DefaultMessagesActivity.open(this,resumeDeliveryRecordVO.getUserId() , resumeDeliveryRecordVO.getUserName(), "");
                break;
        }

    }

    /**
     * 发起修改投递记录请求
     */
    public  void modifyResumeDeliveryRecord(){
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);
        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_UPDATE_DELIVERY_RECORD + token;
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(resumeDelivery)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                @Override
                                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    return new Date(json.getAsJsonPrimitive().getAsLong());
                                }
                            });
                            Gson gson =builder.create();
                            Type jsonType = new TypeToken<TempResponseData<Object>>() {}.getType();
                            final TempResponseData<Object> postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(postResult.getResultCode()==200){
                                        RxToast.success("更新状态成功");
                                        resumeDeliveryRecordVO.setDeliveryStatus(resumeDelivery.getDeliveryStatus());
                                    }else{
                                        RxToast.error("更新状态失败，请重试");
                                    }
                                    resumeDeliveryRecordVO.setDeliveryStatus(resumeDelivery.getDeliveryStatus());
                                    //刷新页面
                                    finish();
                                    Intent intent = new Intent(mContext, ComResumeDeliveryRecordDetailActivity.class);
                                    intent.putExtra("resumeDeliveryRecordVO",resumeDeliveryRecordVO);
                                    startActivity(intent);
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);                                        RxToast.error("更新状态失败，请重试");
                    RxToast.error("更新状态失败，请重试");

                }});

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            RxToast.error("获取数据失败，请重试");
            finish();
            e.printStackTrace();
        }
    }
}
