package com.djylrz.xzpt.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.FocosCompanyListAdapter;
import com.djylrz.xzpt.utils.FocusCompanyItem;
import com.djylrz.xzpt.utils.RecyclerDivider;
import com.djylrz.xzpt.utils.ResumeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FocusCompanyActivity extends BaseActivity {

    private ImageView companyLogo;
    String []companyName;
    private List<FocusCompanyItem> mFocusCompanyItems= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_company);
        //todo 从数据库读关注公司的名字，存入以下数组 ——to小榕
        //logo设置的是int类型，不太清楚从外部怎么传进来
        companyName = new String[] {"阿里巴巴","巨硬","tecent","Apple"};
        initFocusCompanys();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.focus_company_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FocosCompanyListAdapter adapter = new FocosCompanyListAdapter(mFocusCompanyItems);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration (new RecyclerDivider(this, LinearLayoutManager.HORIZONTAL, 3,  R.color.colorDark));
    }

    public void initFocusCompanys() {
        for(int i=0;i<4;i++) {
            //两个参数，一个是int类型的图片，第二个参数是String类型，填进去即可展示列表
            //这个列表的点击事件还没写，因为公司详情页界面没有
            FocusCompanyItem focusCompanyItem =new FocusCompanyItem(R.drawable.apple,companyName[i]);
            mFocusCompanyItems.add(focusCompanyItem);
        }
    }
}
