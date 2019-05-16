package com.djylrz.xzpt.activityCompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
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
import com.vondear.rxui.view.dialog.RxDialogSureCancel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComRecruitmentDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;//标题栏
    private TextView mTvAboutSpannable;//RXTextView布局
    private ComRecruitmentDetailActivity mContext;//上下文context
    private RequestQueue requestQueue;//请求队列
    private RxDialogLoading rxDialogLoadingxx;//加载动画对话框
    private Recruitment recruitment;//用于接收intent传过来的岗位信息对象
    private Button btnEdit;//编辑按钮
    private Button btnOp;//操作按钮（停招岗位为重新发布，在招岗位为结束招聘）
    private boolean flagModify = false;
    private static final String TAG = "ComRecruitmentDetailAct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_recruitment_detail);
        //获取传递过来的岗位信息
        Intent intent = getIntent();
        recruitment = (Recruitment) intent.getSerializableExtra("recruitment");

        //获取布局控件
        toolbar = (Toolbar)findViewById(R.id.asa_toolbar);
        mTvAboutSpannable = (TextView)findViewById(R.id.tv_about_spannable);
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        btnEdit = (Button)findViewById(R.id.btn_edit);
        btnOp = (Button)findViewById(R.id.btn_stop);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("岗位详情");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置加载动画
        rxDialogLoadingxx = new RxDialogLoading(this);
        rxDialogLoadingxx.setLoadingText("拼命加载中");
        rxDialogLoadingxx.setLoadingColor(R.color.colorPrimary);

        //设置按钮
        if(recruitment.getValidate()==0){
            btnEdit.setText("编辑岗位");
            btnEdit.setTextColor(getResources().getColor(R.color.blue));
            btnOp.setText("结束招聘");
            btnOp.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            btnEdit.setText("删除岗位");
            btnEdit.setTextColor(getResources().getColor(R.color.red));
            btnOp.setText("重新招聘");
            btnOp.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        btnEdit.setOnClickListener(this);
        btnOp.setOnClickListener(this);
        initView();
        mContext = this;
        RxTool.init(this);
    }

    private void initView() {
        //行业标签数据
        List<String> data = new ArrayList<>();
        data.add("开发|测试|运维类");
        data.add("产品|需求|项目类");
        data.add("运营|编辑|客服类");
        data.add("市场|商务类");
        data.add("销售类");
        data.add("综合职能|高级管理类");
        data.add("金融类");
        data.add("文娱|传媒|艺术|体育类");
        data.add("教育|培训类");
        data.add("商业服务|专业服务类");
        data.add("贸易|批发|零售|租赁业类");
        data.add("交通|运输|物流|仓储类");
        data.add("房地产|建筑|物业类");
        data.add("生产|加工|制造类");
        data.add("能源矿产|农林牧渔类");
        data.add("化工|生物|制药|医护类");
        data.add("公务员|其他类");
        //获取传递过来的岗位信息
        Intent intent = getIntent();
        recruitment = (Recruitment) intent.getSerializableExtra("recruitment");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-01 hh:mm:ss EE");
        String workTime;
        String industryLabel = data.get((int) recruitment.getIndustryLabel());
        String jobType;
        String recruitmentStatus;//简历状态
        switch ((int)recruitment.getValidate()){
            case 0:
                recruitmentStatus = "正在招聘";
                break;
            case 1:
                recruitmentStatus = "停止招聘";
                break;
            default:
                recruitmentStatus = "停止招聘";
                break;
        }
        switch ((int)recruitment.getJobType()){
            case 1:
                jobType = "招聘";
                break;
            case 2:
                jobType = "实习";
                break;
            case 3:
                jobType = "兼职";
                break;
            default:
                jobType = "未定义";
                    break;
        }
        switch (Integer.parseInt(recruitment.getWorkTime()+"")){
            case 1:
                workTime = "955";
                break;
            case 2:
                workTime = "965";
                break;
            case 3:
                workTime = "956";
                break;
            case 4:
                workTime = "996";
                break;
            default:
                workTime = "955";
                break;
        }
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                RxToast.showToast(ComRecruitmentDetailActivity.this, "事件触发了", 500);
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
                .append(recruitment.getJobName() + "\n").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.colorPrimary))
                .append("发布时间： "+df2.format(recruitment.getPublishTime())+ "\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)
                .append("发布公司： "+recruitment.getCompanyName()+ "\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)

                .append("状态：" + recruitmentStatus +"\n")
                .setBold().setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .setBackgroundColor(getResources().getColor(R.color.lightblue))
                .setForegroundColor(getResources().getColor(R.color.black))

                .append("岗位类型"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(jobType + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("岗位描述"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getDescription() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("工作地点"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getLocation() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("投递要求"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getDeliveryRequest() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("月薪资"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getSalary() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("工作时间"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getDegree() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("月薪资"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(workTime + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("所属行业"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(industryLabel + "\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .into(mTvAboutSpannable);
    }

    /**
     * 发起修改招聘信息状态请求
     */
    public  void modifyRecruitment(){
        //暂时将发布时间修改为空
        recruitment.setPublishTime(null);
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);

        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_UPDATE_RESRUITMENT + token + "&recruitmentId=" + recruitment.getRecruitmentId();
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(recruitment)),
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
                                    //关闭加载动画
                                    if(postResult.getResultCode()==200){
                                        Toast.makeText(mContext, "更新状态成功", Toast.LENGTH_SHORT).show();
                                        //刷新页面
                                        getNewRecruitment();
                                    }else{
                                        Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
                }});

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void getNewRecruitment(){
        //暂时将发布时间修改为空
        recruitment.setPublishTime(null);
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);

        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_GET_RESRUITMENT + token + "&recruitmentId=" + recruitment.getRecruitmentId();
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(recruitment)),
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
                            Type jsonType = new TypeToken<TempResponseData<Recruitment>>() {}.getType();
                            final TempResponseData<Recruitment> postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            recruitment = postResult.getResultObject();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(postResult.getResultCode()==200){
                                        finish();
                                        Intent intent = new Intent(mContext, ComRecruitmentDetailActivity.class);
                                        intent.putExtra("recruitment",recruitment);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
                }});

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    /**
     * 发起删除招聘信息状态请求
     */
    public void deleteRecruitment(){
        //暂时将发布时间修改为空
        recruitment.setPublishTime(null);
        //获取token
        SharedPreferences preferences = getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);

        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_DELETE_RECRUITMENT + token + "&recruitmentId=" + recruitment.getRecruitmentId();
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(recruitment)),
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
                            Type jsonType = new TypeToken<TempResponseData<Recruitment>>() {}.getType();
                            final TempResponseData<Recruitment> postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            recruitment = postResult.getResultObject();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(postResult.getResultCode()==200){
                                        finish();
                                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(mContext, "删除失败，请联系客服", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(mContext, "删除失败，请联系客服", Toast.LENGTH_LONG).show();
                }});

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(mContext, "删除失败，请检查网络", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch ((int)v.getId()){
            case R.id.btn_edit:
                //第一个按钮
                if(recruitment.getValidate()==0){
                    Intent intent = new Intent(this, AddRecruitmentActivity.class);
                    intent.putExtra("recruitment",recruitment);
                    startActivity(intent);
                }else{
                    //提示弹窗
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
                    rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
                    rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteRecruitment();
                        }
                    });
                    rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();
                    break;
                }
                break;
            case R.id.btn_stop:
                //第二个按钮
                if(recruitment.getValidate()==0){
                    recruitment.setValidate(1);
                }else if(recruitment.getValidate()==1){
                    recruitment.setValidate(0);
                }
                Log.d(TAG, "onClick: "+recruitment.getValidate());
                modifyRecruitment();
                break;
        }
    }
}
