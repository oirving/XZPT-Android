package com.djylrz.xzpt.activityStudent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResumeModelHistoryActivity extends AppCompatActivity {
    private static final String TAG = "ResumeModelHistoryActivity";
    private List<ResumeModelItem> resumeModelIHistorytemList = new ArrayList<>();
    private ResumeModelListAdapter resumeModelIHistorytemListAdapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_model_history);
        initResumeModel();
        toolbar = (Toolbar) findViewById(R.id.resume_toolbar);
        resumeModelIHistorytemListAdapter = new ResumeModelListAdapter(resumeModelIHistorytemList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.resume_model_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resumeModelIHistorytemListAdapter);
    }

    //todo 把历史记录中简历的缩略图放进来，图片传输有待更改，目前只放了本地R类的文件
    public void initResumeModel() {
        for(int i =0;i<3;i++) {
            ResumeModelItem resumeModelItem = new ResumeModelItem(R.drawable.resumemodel2);
            resumeModelIHistorytemList.add(resumeModelItem);
        }
    }
}
