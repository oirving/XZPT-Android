package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.HashSet;
import java.util.Set;

public class SelectTagActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TagFlowLayout mFlowLayout;
    private Set<Integer> selectTagSet;
    private String tagIndex;
    private String tagNames;
    private String[] mVals = new String[]
            {"C++","Javascript","金融","直播","电商","Java","移动互联网","分布式","C","服务器端","社交",
                    "带薪年假","银行","云计算","MySQL","Linux/Unix","旅游","绩效奖金","工具软件","大数据",
                    "系统架构","互联网金融","J2EE","Hadoop","中间件","支付","顶尖团队","医疗健康","牛人多",
                    "ERP","新零售","平台","数据库","企业服务","PHP","汽车","五险一金","节日礼物","本地生活",
                    "软件开发","移动开发","招聘","Python","广告营销","Yii","保险","SOA","Node.js","IOS",
                    "Golang","嵌入式","Phalcon","Laravel","硬件制造","HTML","客户端","美女CEO","数据处理",
                    "股票期权","无限量零食","抓取","docker","前端开发","数据挖掘","高级技术管理","GO","Web前端",
                    "架构师","文体活动","信息安全","Ruby","运维","爬虫工程师","自动化","图像处理","MFC","游戏",
                    "即时通讯","C#/.NET","通信","媒体","发展空间大","Windows","视频","两次年度旅游","通信/网络设备",
                    "定期体检","年底双薪","教育","JS","现象级产品","地图","算法","视觉","品牌","用户体验","理财",
                    "UI","UED","画册","动画","平面","网店","原画","创意","UE","App设计","Flash","3D","包装","借贷",
                    "美工","广告","交互设计专家","餐补交通补","福利优厚","工程师文化","超豪华办公室","专项奖金",
                    "岗位晋升","Android","全栈","HTML5","爬虫","领导力","团建旅游","Shell","扁平管理","年度旅游",
                    "爬虫架构","搜索","技能培训","音视频","QT","视频流转码","视频编解码","简单有爱文化","福利倍儿好",
                    "免费班车","年终分红","语音处理","机器学习","移动交互","电商美工","网页","手绘","场景","UX",
                    "部门管理","Scala","交通补助","过节费","弹性工作","午餐补助","创业公司范儿","网络爬虫","区块链",
                    "团队建设","美味晚餐","领导好","音频编解码","弹性工作制","年终奖丰厚","Q版","2D","多媒体","目标管理"
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectTagSet= new HashSet<>();
        tagIndex = "";
        tagNames = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        //获取布局空间
        toolbar = (Toolbar)findViewById(R.id.select_tag_toolbar);
        mFlowLayout = (TagFlowLayout)findViewById(R.id.id_flowlayout);
        final LayoutInflater mInflater = LayoutInflater.from(this);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("选择岗位标签");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseSelectTags();
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.done_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_menu_done:
                        //完成选择标签
                        //解析数据
                        parseSelectTags();
                        //返回上一层activity
                        onBackPressed();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                //Toast.makeText(SelectTagActivity.this, mVals[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                Toast.makeText(SelectTagActivity.this, selectPosSet.toString(), Toast.LENGTH_SHORT).show();
                selectTagSet = selectPosSet;
            }
        });
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    private void parseSelectTags(){
        for(Integer tag : selectTagSet){
            if(tagIndex.equals("")){
                tagIndex = tag+"";
            }else{
                tagIndex = tagIndex + "," + tag;
            }
            if(tagNames.equals("")){
                tagNames =  mVals[tag];
            }else{
                tagNames = tagNames + "," + mVals[tag];
            }
        }
    }

    /**
     * 重写该界面的返回方法
     * 直接返回默认选择第一项
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("number", tagIndex);
        intent.putExtra("names", tagNames);
        setResult(0, intent);
        finish();
    }
}
