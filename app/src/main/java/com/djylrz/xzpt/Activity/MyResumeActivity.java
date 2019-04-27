package com.djylrz.xzpt.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.ResumeItem;
import com.djylrz.xzpt.utils.ResumeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyResumeActivity extends AppCompatActivity {

    private List<ResumeItem>mResumeList = new ArrayList<>();
    private String[] state;//简历状态
    private String[] position;//申请职位
    private String userName;//用户名称
    private String[] time;//申请时间
    private int numOfResume;//简历份数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume);
        //todo 从系统读取简历基本状态——to小榕
        state = new String[]{"通过","未通过","待审核","asdjha"};//简历状态
        position = new String[]{};//申请职位
        userName = new String();//用户名
        time = new String[]{};//申请时间
        numOfResume=4;//简历份数！！！
        initResumeList();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.myresume_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ResumeListAdapter adapter = new ResumeListAdapter(mResumeList);
        recyclerView.setAdapter(adapter);

    }

    private void initResumeList() {
        for(int i=0;i<numOfResume;i++) {
            //信息填入，上面的参数填好就不用管这里了，没填好之前点击会崩
            ResumeItem resumeItem = new ResumeItem(state[i],position[i],userName,time[i]);
            mResumeList.add(resumeItem);
        }
    }
}
