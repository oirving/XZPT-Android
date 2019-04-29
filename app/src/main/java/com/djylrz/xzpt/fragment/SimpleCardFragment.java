package com.djylrz.xzpt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("ValidFragment")
public class SimpleCardFragment extends Fragment {
    private String mTitle;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
    private int type = 9999;
    RecruitmentAdapter adapter;

    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        if(title.equals("已发布岗位")){
            sf.type =0;
        }else if(title.equals("已停招岗位")){
            sf.type =1;
        }
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        initRecruitments();
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecruitmentAdapter(recruitmentList,type);
        recyclerView.setAdapter(adapter);

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);

                //假设最多加载52条item
                if (recruitmentList.size() < 52) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initRecruitments();
                                    adapter.setLoadState(adapter.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                } else {
                    // 显示加载到底的提示
                    adapter.setLoadState(adapter.LOADING_END);
                }
            }
        });

        return v;
    }

    private void initRecruitments(){
        for(int i = 0; i< 20 ; ++i){
            Recruitment test1 = new Recruitment(i,java.sql.Timestamp.valueOf("2019-01-01 15:54:21.0"),1,"福州大学",
                    "大二辅导员","xxxxxxx","15659769111","福州","中文简历","100K-120K","博士以上",996,1,"开发",1);
            recruitmentList.add(test1);
        }
    }
}