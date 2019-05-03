package com.djylrz.xzpt.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ResumeDeliveryRecordDetialActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTvAboutSpannable;
    private ResumeDeliveryRecordDetialActivity mContext;

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

        //获取传递过来的岗位信息
        Intent intent = getIntent();
        recruitment = (Recruitment) intent.getSerializableExtra("recruitment");


        // 响应点击事件的话必须设置以下属性
        mTvAboutSpannable.setMovementMethod(LinkMovementMethod.getInstance());
//
//        RxTextTool.getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
//                .append(recruitment.getJobName() + "\n").setAlign(Layout.Alignment.ALIGN_CENTER).setForegroundColor(getResources().getColor(R.color.colorPrimary))
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
//                .into(mTvAboutSpannable);
    }
}
