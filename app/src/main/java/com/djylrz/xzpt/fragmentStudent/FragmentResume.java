package com.djylrz.xzpt.fragmentStudent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentResume extends Fragment {
    private static final String TAG = "FragmentResume";
    private List<ResumeModelItem> resumeModelItemList = new ArrayList<>();
    private ResumeModelListAdapter resumeModelListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_resume,container,false);
        initResumeModel();
        resumeModelListAdapter = new ResumeModelListAdapter(resumeModelItemList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.resume_model_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resumeModelListAdapter);
        return view;
    }

    public void initResumeModel() {
        for(int i =0;i<20;i++) {
            ResumeModelItem resumeModelItem = new ResumeModelItem(R.drawable.resumemodel2);
            resumeModelItemList.add(resumeModelItem);
        }
    }
}
