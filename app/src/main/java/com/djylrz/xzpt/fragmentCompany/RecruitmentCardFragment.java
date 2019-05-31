package com.djylrz.xzpt.fragmentCompany;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.TempResponseRecruitmentData;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.RecruitmentAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressLint("ValidFragment")
public class RecruitmentCardFragment extends Fragment {
    private String mTitle;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
    private int type = 9999;
    private RecruitmentAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private static final String TAG = "RecruitmentCardFragment";
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private long limitNum = 9999;

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
    public void onResume() {
        super.onResume();
        // 刷新数据
        recruitmentList.clear();
        currentPage = 1;
        initRecruitments();
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_recruitment_card, null);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        //加载数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new RecruitmentAdapter(recruitmentList,type,getContext());
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
                currentPage = 1;
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
                if (recruitmentList.size() < limitNum) {
                    initRecruitments();
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
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                @Override
                                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    return new Date(json.getAsJsonPrimitive().getAsLong());
                                }
                            });
                            Gson gson =builder.create();
                            Type jsonType = new TypeToken<TempResponseData<TempResponseRecruitmentData<List<Recruitment>>>>() {}.getType();
                            final TempResponseData<TempResponseRecruitmentData<List<Recruitment>> > postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            if(postResult.getResultCode().equals(200)){
                                TempResponseRecruitmentData<List<Recruitment>>  resultObject = postResult.getResultObject();
                                List<Recruitment> recruitments = resultObject.getContentList();
                                if(recruitments != null){
                                    for (int i = 0; i < recruitments.size(); ++i) {
                                        if(recruitments.get(i).getValidate()==type){
                                            recruitmentList.add(recruitments.get(i));
                                        }
                                    }
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(postResult.getResultCode().equals(200)){
                                        loadMoreWrapper.notifyDataSetChanged();
                                    }else{
                                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                                        limitNum = recruitmentList.size();
                                    }
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }});
            //设置超时时间
            jsonObjectRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            20000,//默认超时时间，应设置一个稍微大点儿的，十秒
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
            RxToast.warning("当前网络状态不好，可能导致加载缓慢！");
        }
    }
}
