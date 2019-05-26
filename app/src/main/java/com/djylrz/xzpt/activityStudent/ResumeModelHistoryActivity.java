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
        //todo 没有把简历模版resumeTemplate赋给ResumeModeItem
        //todo 需要从后端获取导出的简历的缩略图和导出的简历的下载链接
        //todo 导出简历接口接收模版文件名，返回生成简历word文件路径
        //todo 获取简历模版导出历史记录——后端接口无

        //todo 历史记录接口接收用户ID，返回已经生成的所有简历的下载链接和简历所应用的模版的缩略图链接，查看的时候需要重新下载
        //todo 历史记录查看所有本地已经下载的简历，因为导出过的简历都会下载到本地，因此查看无需下载
    }
}
