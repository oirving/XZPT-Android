package com.djylrz.xzpt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.utils.StudentRecruitmentAdapter;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class RecommendCardFragment extends Fragment {
    private String mTitle;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();

    public static RecommendCardFragment getInstance(String title) {
        RecommendCardFragment sf = new RecommendCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.job_recommend_card, null);
        initRecruitments();
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        StudentRecruitmentAdapter adapter = new StudentRecruitmentAdapter(recruitmentList,0);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void initRecruitments(){
        for(int i = 0; i< 20 ; ++i){
            Recruitment test1 = new Recruitment(i,java.sql.Timestamp.valueOf("2019-01-01 15:54:21.0"),1,"待就业六人组",
                    "Java实习生","xxxxxxx","15659769111","福州","中文简历","100K-120K","博士以上",996,1,"开发",1);
            recruitmentList.add(test1);
        }
    }
}