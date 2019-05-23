package com.djylrz.xzpt.fragmentStudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.ResumeModelHistoryActivity;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentResume extends Fragment {
    private static final String TAG = "FragmentResume";
    private List<ResumeModelItem> resumeModelItemList = new ArrayList<>();
    private ResumeModelListAdapter resumeModelListAdapter;
    private Toolbar toolbar;


    /**
     * Fragment中初始化Toolbar
     * @param toolbar
     * @param title 标题
     * @param isDisplayHomeAsUp 是否显示返回箭头
     */
    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2_resume,container,false);
        initResumeModel();
        toolbar = (Toolbar) view.findViewById(R.id.resume_toolbar);
        initToolbar(toolbar,"",false);
        setHasOptionsMenu(true);
        resumeModelListAdapter = new ResumeModelListAdapter(resumeModelItemList);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.resume_model_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resumeModelListAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resume_history_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Toast.makeText(getContext(),"menu",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ResumeModelHistoryActivity.class);
                startActivity(intent);
        return  true;
    }

    public void initResumeModel() {
        for(int i =0;i<20;i++) {
            ResumeModelItem resumeModelItem = new ResumeModelItem(R.drawable.resumemodel2);
            resumeModelItemList.add(resumeModelItem);
        }
    }
}
