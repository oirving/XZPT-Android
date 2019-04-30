package com.djylrz.xzpt.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.RecruitmentDateAdapter;
import com.djylrz.xzpt.utils.RecruitmentDateItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentDate extends Fragment implements  View.OnClickListener{
    private List<RecruitmentDateItem> recruitmentDateItems=new ArrayList<>();

    ImageView searchLogo;//查询logo
    EditText searchInput;//查询输入框
    Button searchButton;//查询按钮

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_date,container,false);
        searchLogo = (ImageView) view.findViewById(R.id.recruitment_search_imageview);
        searchLogo.setOnClickListener(this);
        searchButton = (Button) view.findViewById(R.id.recruitment_search_button);
        searchButton.setOnClickListener(this);
        searchInput = (EditText) view.findViewById(R.id.recruitment_search_input_edittext);
        initRecruitmenDate();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recruitment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecruitmentDateAdapter mRecruitmentDateAdapter= new RecruitmentDateAdapter(recruitmentDateItems);
        recyclerView.setAdapter(mRecruitmentDateAdapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recruitment_search_imageview:
                if(searchInput.getVisibility()==View.VISIBLE||searchButton.getVisibility()==View.VISIBLE) {
                    searchButton.setVisibility(View.INVISIBLE);
                    searchInput.setVisibility(View.INVISIBLE);
                } else {
                    searchButton.setVisibility(View.VISIBLE);
                    searchInput.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.recruitment_search_button:
                //todo 返回查询结果
                break;
        }
    }

    public void initRecruitmenDate() {
        for(int i=0;i<40;i++) {
            RecruitmentDateItem recruitmentDateItem = new RecruitmentDateItem("福州大学","待就业公司招聘会","2019-5-20 下午三四节");
            recruitmentDateItems.add(recruitmentDateItem);
        }

    }

}
