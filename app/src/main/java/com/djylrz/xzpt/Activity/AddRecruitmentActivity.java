package com.djylrz.xzpt.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.SinglePicker;


public class AddRecruitmentActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    //城市id
    private String id;
    //城市name
    private String name;
    private Activity activity;
    private RelativeLayout layoutLocation;
    private TextView textViewLocation;
    private RelativeLayout layoutDegree;
    private TextView textViewDegree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recruitment);
        activity = this;
        //获取布局控件
        toolbar = (Toolbar)findViewById(R.id.asa_toolbar);
        layoutLocation = findViewById(R.id.job_location_layout);
        textViewLocation = findViewById(R.id.job_location_result);
        layoutDegree = findViewById(R.id.job_degree_layout);
        textViewDegree = findViewById(R.id.job_degree_result);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("发布岗位");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.done_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_menu_done:
                        //发布岗位
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //添加监听
        layoutLocation.setOnClickListener(this);
        layoutDegree.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //地点
            case R.id.job_location_layout:
                JDCityPicker cityPicker = new JDCityPicker();
                cityPicker.init(activity);
                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    //回调函数
                    @Override
                    public void onSelected(ProvinceBean province, com.lljjcoder.bean.CityBean city, DistrictBean district) {
                        textViewLocation.setText(province.getName() + city.getName() + district.getName());
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                cityPicker.showCityPicker();
                break;
            case R.id.job_degree_layout:
                List<String> data = new ArrayList<>();
                data.add("无要求");
                data.add("初中及以上");
                data.add("中专/中技及以上");
                data.add("高中及以上");
                data.add("大专及以上");
                data.add("本科及以上");
                data.add("硕士及以上");
                data.add("博士及以上");
                SinglePicker<String> picker = new SinglePicker<>(this, data);
                picker.setCanceledOnTouchOutside(false);
                picker.setSelectedIndex(1);
                picker.setCycleDisable(true);
                picker.setCanceledOnTouchOutside(true);
                picker.setTextSizeAutoFit(true);
                picker.setLabelTextColor(R.color.colorPrimary);
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {
                        textViewDegree.setText(item);
                        Toast.makeText(activity, item, Toast.LENGTH_SHORT).show();
                    }
                });
                picker.show();
                break;
        }
    }
}
