package com.djylrz.xzpt.fragmentStudent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.InterviewTipsAdapter;
import com.djylrz.xzpt.utils.InterviewTipsItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentTips extends Fragment {
    private List<InterviewTipsItem> interviewTipsItems=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4_tips,container,false);
        initTips();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.interview_tips_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        InterviewTipsAdapter interviewTipsAdapter= new InterviewTipsAdapter(interviewTipsItems);
        recyclerView.setAdapter(interviewTipsAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void initTips() {
        for(int i=0;i<40;i++) {
            InterviewTipsItem interviewTipsItem = new InterviewTipsItem("自信最重要");
            interviewTipsItems.add(interviewTipsItem);
        }

    }

}
