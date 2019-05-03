package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;
import com.vondear.rxtool.RxTextTool;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.view.RxToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ComRecruitmentDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTvAboutSpannable;
    private ComRecruitmentDetailActivity mContext;

    private Recruitment recruitment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detial);
        //获取布局控件
        toolbar = (Toolbar)findViewById(R.id.asa_toolbar);
        mTvAboutSpannable = (TextView)findViewById(R.id.tv_about_spannable);
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
        RxTool.init(this);
        initView();
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
                .append("发布公司： "+recruitment.getCompanyName()+ "\n\n\n").setFontFamily("serif").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.black)).setProportion((float)0.8)

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
                .append(industryLabel + "\n\n").setLeadingMargin(60, 50).setProportion((float)0.8).setForegroundColor(getResources().getColor(R.color.black))

                .into(mTvAboutSpannable);
    }
}
