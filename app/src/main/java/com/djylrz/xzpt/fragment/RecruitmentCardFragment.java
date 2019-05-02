package com.djylrz.xzpt.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.RecruitmentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("ValidFragment")
public class RecruitmentCardFragment extends Fragment {
    private String mTitle;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
    private int type = 9999;
    private RecruitmentAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView avi;
    private static final String TAG = "RecruitmentCardFragment";
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private RequestQueue requestQueue;

    public static RecruitmentCardFragment getInstance(String title) {
        RecruitmentCardFragment sf = new RecruitmentCardFragment();
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
        View v = inflater.inflate(R.layout.fr_recruitment_card, null);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        avi = v.findViewById(R.id.avi_com_home);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); //把上下文context作为参数传递进去

        //开始加载动画
        startAnim();

        //加载数据
        initRecruitments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new RecruitmentAdapter(recruitmentList,type);
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(loadMoreWrapper);
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                recruitmentList.clear();
                initRecruitments();
                loadMoreWrapper.notifyDataSetChanged();

                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (recruitmentList.size() < 52) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initRecruitments();
                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                } else {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });
        return v;
    }

    private void initRecruitments(){
        //获取token
        SharedPreferences preferences = getActivity().getSharedPreferences("token",0);
        String token = preferences.getString(PostParameterName.TOKEN,null);
        //组装URL
        String url = PostParameterName.POST_URL_COMPANY_GET_RECRUITMENT_LIST + token ;
        //
        PageData pageData = new PageData();
        pageData.setCurrentPage(currentPage++);
        pageData.setPageSize(PAGE_SIZE);
        //请求数据
        try {
            Log.d(TAG, "onCreate: 开始发送json请求"+ url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,new JSONObject(new Gson().toJson(pageData)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回"+response.toString());
                            Type jsonType = new TypeToken<TempResponseData<List<Recruitment>>>() {}.getType();
                            final TempResponseData<List<Recruitment>> postResult = new Gson().fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            List<Recruitment> recruitments= postResult.getResultObject();
                            for (Recruitment r:recruitments) {
                                if(r.getValidate()==type){
                                    recruitmentList.add(r);
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //结束加载动画
                                    stopAnim();
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }});
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    /**
     *
     */
    public void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }
}