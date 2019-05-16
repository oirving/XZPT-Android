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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.StudentRecruitmentAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class RecommendCardFragment extends Fragment {
    private static final String TAG = "RecommendCardFragment";
    private String mTitle;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
    private User user = new User();
    private StudentRecruitmentAdapter adapter;
    private RecyclerView recyclerView;
    private LoadMoreWrapper loadMoreWrapper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private long limitNum = 9999;

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
        initRecruitments();//获取推荐信息
        adapter = new StudentRecruitmentAdapter(recruitmentList, 0);
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
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

    private void initRecruitments() {
       /* for(int i = 0; i< 20 ; ++i){
            Recruitment test1 = new Recruitment(i,java.sql.Timestamp.valueOf("2019-01-01 15:54:21.0"),1,"待就业六人组",
                    "Java实习生","xxxxxxx","15659769111","福州","中文简历","100K-120K","博士以上",996,1,"开发",1);
            recruitmentList.add(test1);
        }*/
        if (mTitle.equals("推荐")) {
            //查询招聘推荐并显示
            VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
            SharedPreferences userToken = getContext().getSharedPreferences("token", 0);
            String token = userToken.getString(PostParameterName.STUDENT_TOKEN, null);
            if (token != null) {
                user.setToken(userToken.getString(PostParameterName.STUDENT_TOKEN, null));

                try {
                    Log.d(TAG, "initRecruitments: " + PostParameterName.POST_URL_GET_RECOMMEND + user.getToken());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_RECOMMEND + user.getToken(), new JSONObject(new Gson().toJson(user)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: 返回" + response.toString());
                                    /* recruitmentList = new Gson().fromJson(stringJson, new TypeToken<List<Recruitment>>(){}.getType());*/
                                    try {
                                        switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                            case "200": {
                                                JSONArray jsonArray = response.getJSONArray("resultObject");
                                                //recruitmentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Recruitment>>(){}.getType());

                                                GsonBuilder builder = new GsonBuilder();
                                                builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                                    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                        return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                                    }
                                                });
                                                Gson gson = builder.create();

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Log.d(TAG, "onResponse: " + jsonArray.getJSONObject(i).toString());
                                                    Recruitment tempRecruitment = gson.fromJson(jsonArray.getJSONObject(i).toString(), Recruitment.class);
                                                    recruitmentList.add(tempRecruitment);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    RecommendCardFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG Response failed", error.getMessage(), error);
                        }
                    });

                    VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
                    VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "initRecruitments: 没有获取到token");
            }
        }else if (mTitle.equals("热门")){
            //查询热门招聘并显示
            VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
            SharedPreferences userToken = getContext().getSharedPreferences("token", 0);
            String token = userToken.getString(PostParameterName.STUDENT_TOKEN, null);
            if (token != null) {
                user.setToken(userToken.getString(PostParameterName.STUDENT_TOKEN, null));

                try {
                    Log.d(TAG, "initRecruitments: " + PostParameterName.POST_URL_GET_HOT_RECRUIMENT + user.getToken());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_HOT_RECRUIMENT + user.getToken(), new JSONObject(new Gson().toJson(user)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: 返回" + response.toString());
                                    /* recruitmentList = new Gson().fromJson(stringJson, new TypeToken<List<Recruitment>>(){}.getType());*/
                                    try {
                                        switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                            case "200": {
                                                JSONArray jsonArray = response.getJSONArray("resultObject");
                                                //recruitmentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Recruitment>>(){}.getType());

                                                GsonBuilder builder = new GsonBuilder();
                                                builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                                    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                        return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                                    }
                                                });
                                                Gson gson = builder.create();

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Log.d(TAG, "onResponse: " + jsonArray.getJSONObject(i).toString());
                                                    Recruitment tempRecruitment = gson.fromJson(jsonArray.getJSONObject(i).toString(), Recruitment.class);
                                                    recruitmentList.add(tempRecruitment);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    RecommendCardFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter = new StudentRecruitmentAdapter(recruitmentList, 0);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG Response failed", error.getMessage(), error);
                        }
                    });

                    VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
                    VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "initRecruitments: 没有获取到token");
            }
        }
    }

    public void updateAdapter(List<Recruitment> list) {
        adapter = new StudentRecruitmentAdapter(list, 0);
        recyclerView.setAdapter(adapter);
    }
}
