package com.djylrz.xzpt.fragment.student;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.StudentRecruitmentAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    public void onResume() {
        super.onResume();
        // 刷新数据
        recruitmentList.clear();
        currentPage = 1;
        limitNum = 9999;
        initRecruitments();
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.job_recommend_card, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new StudentRecruitmentAdapter(recruitmentList, 0);
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setAdapter(loadMoreWrapper);
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
                                    final JSONObject responseObject = response;
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
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                                                        limitNum = recruitmentList.size();
                                                        loadMoreWrapper.notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                            break;
                                            case "2018":
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                                                        limitNum = recruitmentList.size();
                                                        loadMoreWrapper.notifyDataSetChanged();
                                                    }
                                                });
                                                break;
                                            default:
                                                RxToast.error("服务器返回数据异常！错误代码：" + response.getString(PostParameterName.RESPOND_RESULTCODE));
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        RxToast.error("无法连接服务器！");
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG Response failed", error.getMessage(), error);
                            RxToast.error("无法连接服务器！");
                        }
                    });
                    //设置超时时间
                    jsonObjectRequest.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    10000,//默认超时时间，应设置一个稍微大点儿的，十秒
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

            } else {
                Log.d(TAG, "initRecruitments: 没有获取到token");
            }
        } else if (mTitle.equals("热门")) {
            //查询热门招聘并显示
            //获取token
            SharedPreferences preferences = getActivity().getSharedPreferences("token", 0);
            String token = preferences.getString(PostParameterName.STUDENT_TOKEN, null);
            //组装URL
            String url = PostParameterName.POST_URL_GET_HOT_RECRUIMENT + token;
            //
            PageData pageData = new PageData();
            pageData.setCurrentPage(currentPage++);
            pageData.setPageSize(PAGE_SIZE);
            //请求数据
            try {
                Log.d(TAG, "onCreate: 开始发送json请求" + url);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(new Gson().toJson(pageData)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                try {
                                    if (response.getString(PostParameterName.RESPOND_RESULTCODE).equals("200")) {
                                        GsonBuilder builder = new GsonBuilder();
                                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                            @Override
                                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                                return new Date(json.getAsJsonPrimitive().getAsLong());
                                            }
                                        });
                                        Gson gson = builder.create();
                                        Type jsonType = new TypeToken<TempResponseData<List<Recruitment>>>() {
                                        }.getType();
                                        final TempResponseData<List<Recruitment>> postResult = gson.fromJson(response.toString(), jsonType);
                                        Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                        if (postResult.getResultCode().equals(200)) {
                                            List<Recruitment> recruitments = postResult.getResultObject();
                                            if (recruitments != null) {
                                                for (int i = 0; i < recruitments.size(); ++i) {
                                                    recruitmentList.add(recruitments.get(i));
                                                }
                                            }
                                        }
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (loadMoreWrapper != null) {
                                                    loadMoreWrapper.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    } else if (response.getString(PostParameterName.RESPOND_RESULTCODE).equals("2018")) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (loadMoreWrapper != null) {
                                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                                                    limitNum = recruitmentList.size();
                                                }
                                            }
                                        });
                                    } else {
                                        RxToast.error("服务器返回数据异常！错误代码：" + response.getString(PostParameterName.RESPOND_RESULTCODE));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    RxToast.error("无法连接服务器！");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        RxToast.error("无法连接服务器！");
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
                RxToast.warning("当前网络状态不好，可能导致加载缓慢！");
            }
        }
    }

    public void updateAdapter(List<Recruitment> list) {
        adapter = new StudentRecruitmentAdapter(list, 0);
        recyclerView.setAdapter(adapter);
    }
}
