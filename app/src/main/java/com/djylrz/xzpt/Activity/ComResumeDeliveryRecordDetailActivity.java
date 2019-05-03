package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.vo.ResumeDeliveryRecordVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.RxTextTool;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class ComResumeDeliveryRecordDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTvAboutSpannable;
    private ComResumeDeliveryRecordDetailActivity mContext;
    private RequestQueue requestQueue;
    private ResumeDeliveryRecordVO resumeDeliveryRecordVO;
    private RxDialogLoading rxDialogLoading;
    private Resume resume;
    private Button btnRefuse;
    private Button btnNext;
    private static final String TAG = "ComResumeDeliveryRecord";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_delivery_record_detial);
        //获取布局控件
        toolbar = (Toolbar)findViewById(R.id.asa_toolbar);
        mTvAboutSpannable = (TextView)findViewById(R.id.tv_about_spannable);
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        btnRefuse = (Button)findViewById(R.id.btn_refuse);
        btnNext = (Button)findViewById(R.id.btn_next);
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
        rxDialogLoading.setLoadingText("正在获取简历数据");
        rxDialogLoading.setLoadingColor(R.color.colorPrimary);

        //设置按钮
        btnRefuse.setText("拒绝");
        btnRefuse.setTextColor(getResources().getColor(R.color.red));
        btnNext.setText("下一步");
        btnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        mContext = this;
        RxTool.init(this);
        initView();
    }

    private void initView() {

        //获取传递过来的岗位信息
        Intent intent = getIntent();
        resumeDeliveryRecordVO = (ResumeDeliveryRecordVO) intent.getSerializableExtra("resumeDeliveryRecordVO");
        //根据resumeId获取简历详细信息
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);
        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_GET_RESUME_BY_ID + token + "&resumeId=" + resumeDeliveryRecordVO.getResumeId();
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            rxDialogLoading.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(resumeDeliveryRecordVO)),
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
                                    rxDialogLoading.hide();
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    rxDialogLoading.hide();
                    Toast.makeText(mContext, "获取数据失败，请重试", Toast.LENGTH_LONG).show();
                    finish();
                }});
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setText(){
        String sex;
        String highestEducation;
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
                highestEducation = "未填写";
                break;
            case 2:
                highestEducation = "大专及以上";
                break;
            case 3:
                highestEducation = "本科及以上";
                break;
            case 4:
                highestEducation = "硕士及以上";
                break;
            case 5:
                highestEducation = "博士及以上";
                break;
            default:
                highestEducation = "未填写";
                break;
        }
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(mContext, "这里应该跳转到职位详情页，还没写！", Toast.LENGTH_SHORT).show();
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

                .append("基本信息" + "\n").setBold()
                .append("性别：" + sex + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("学校：" + resume.getSchool() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
                .append("专业：" + resume.getSpeciality() + "\n").setBullet(60, getResources().getColor(R.color.colorPrimary)).setProportion((float)0.8)
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

//                .append("发布时间： "+df2.format(recruitment.getPublishTime())+ "\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)
//                .append("发布公司： "+recruitment.getCompanyName()+ "\n\n\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)
//
//                .append("岗位类型"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(jobType + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("岗位描述"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(recruitment.getDescription() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("工作地点"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(recruitment.getLocation() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("投递要求"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(recruitment.getDeliveryRequest() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("月薪资"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(recruitment.getSalary() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("工作时间"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(recruitment.getDegree() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("月薪资"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(workTime + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
//                .append("所属行业"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
//                .append(industryLabel + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
//
                .into(mTvAboutSpannable);
    }
}
