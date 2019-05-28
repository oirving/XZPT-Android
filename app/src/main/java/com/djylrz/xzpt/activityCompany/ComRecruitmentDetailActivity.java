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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.MyApplication;
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
import com.vondear.rxui.view.dialog.RxDialogShapeLoading;
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
    //后续从服务器获取该数据
    private String[] mVals = new String[]
            {"C++", "Javascript", "金融", "直播", "电商", "Java", "移动互联网", "分布式", "C", "服务器端", "社交",
                    "带薪年假", "银行", "云计算", "MySQL", "Linux/Unix", "旅游", "绩效奖金", "工具软件", "大数据",
                    "系统架构", "互联网金融", "J2EE", "Hadoop", "中间件", "支付", "顶尖团队", "医疗健康", "牛人多",
                    "ERP", "新零售", "平台", "数据库", "企业服务", "PHP", "汽车", "五险一金", "节日礼物", "本地生活",
                    "软件开发", "移动开发", "招聘", "Python", "广告营销", "Yii", "保险", "SOA", "Node.js", "IOS",
                    "Golang", "嵌入式", "Phalcon", "Laravel", "硬件制造", "HTML", "客户端", "美女CEO", "数据处理",
                    "股票期权", "无限量零食", "抓取", "docker", "前端开发", "数据挖掘", "高级技术管理", "GO", "Web前端",
                    "架构师", "文体活动", "信息安全", "Ruby", "运维", "爬虫工程师", "自动化", "图像处理", "MFC", "游戏",
                    "即时通讯", "C#/.NET", "通信", "媒体", "发展空间大", "Windows", "视频", "两次年度旅游", "通信/网络设备",
                    "定期体检", "年底双薪", "教育", "JS", "现象级产品", "地图", "算法", "视觉", "品牌", "用户体验", "理财",
                    "UI", "UED", "画册", "动画", "平面", "网店", "原画", "创意", "UE", "App设计", "Flash", "3D", "包装", "借贷",
                    "美工", "广告", "交互设计专家", "餐补交通补", "福利优厚", "工程师文化", "超豪华办公室", "专项奖金",
                    "岗位晋升", "Android", "全栈", "HTML5", "爬虫", "领导力", "团建旅游", "Shell", "扁平管理", "年度旅游",
                    "爬虫架构", "搜索", "技能培训", "音视频", "QT", "视频流转码", "视频编解码", "简单有爱文化", "福利倍儿好",
                    "免费班车", "年终分红", "语音处理", "机器学习", "移动交互", "电商美工", "网页", "手绘", "场景", "UX",
                    "部门管理", "Scala", "交通补助", "过节费", "弹性工作", "午餐补助", "创业公司范儿", "网络爬虫", "区块链",
                    "团队建设", "美味晚餐", "领导好", "音频编解码", "弹性工作制", "年终奖丰厚", "Q版", "2D", "多媒体", "目标管理"
            };
    private static final String TAG = "ComRecruitmentDetailAct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_recruitment_detail);
        mContext = this;
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
        //设置菜单
        toolbar.inflateMenu(R.menu.com_recruitment_detail_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_menu_done:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //获取传递过来的岗位信息
        Intent intent = getIntent();
        recruitment = (Recruitment) intent.getSerializableExtra("recruitment");
        if(recruitment == null){
            //开始加载动画
            MyApplication.rxDialogShapeLoading = new RxDialogShapeLoading(mContext);
            MyApplication.rxDialogShapeLoading.setLoadingText("正在加载中");
            MyApplication.rxDialogShapeLoading.show();
            recruitment = new Recruitment();
            Long recruitmentId = intent.getLongExtra("recruitmentId",0);
            recruitment.setRecruitmentId(recruitmentId);
            getNewRecruitment();
        }else{
            initView();
        }
    }

    private void initView() {
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
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EE");
        String workTime;
        String industryLabel = "";
        if(recruitment.getIndustryLabel() > 0){
            industryLabel = data.get((int) recruitment.getIndustryLabel()-1);
        }else{
            industryLabel = "暂未分类";
        }

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
        //获取岗位标签名称
        String strStationLabels = recruitment.getStationLabel();
        if(strStationLabels != null){
            String[] strStationLabel = strStationLabels.split(",");
            strStationLabels = "";
            if(strStationLabel.length == 1){
                strStationLabels = mVals[Integer.parseInt(strStationLabel[0])-1];
            }else{
                for (String stationLabel : strStationLabel) {
                    strStationLabels = strStationLabels + mVals[Integer.parseInt(stationLabel)-1] + ",";
                }
            }
        }else{
            strStationLabels = "暂未分类";
        }

        RxTextTool.getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
                .append(recruitment.getJobName() + "\n").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.colorPrimary))
                .append("发布时间： "+df2.format(recruitment.getPublishTime())+ "\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)
                .append("发布公司： "+recruitment.getCompanyName()+ "\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)

                .append("状态：" + recruitmentStatus +"\n")
                .setBold().setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .setBackgroundColor(getResources().getColor(R.color.lightblue))
                .setForegroundColor(getResources().getColor(R.color.black))
                .append("招聘人数"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getHeadCount() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))
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

                .append("学历要求"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(recruitment.getDegree() + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("工作时间制度"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(workTime + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("所属行业"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(industryLabel + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .append("岗位标签"+"\n").setBold().setBullet(60, getResources().getColor(R.color.colorPrimary))
                .append(strStationLabels + "\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))



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
                                    //结束加载动画
                                    MyApplication.rxDialogShapeLoading.hide();
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
                    //结束加载动画
                    MyApplication.rxDialogShapeLoading.hide();
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(mContext, "更新状态失败，请重试", Toast.LENGTH_LONG).show();
                }});
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            //结束加载动画
            MyApplication.rxDialogShapeLoading.hide();
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
                    rxDialogSureCancel.getTitleView().setText("确认删除");
                    rxDialogSureCancel.setContent("删除后不可恢复！确认删除该岗位？");
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
