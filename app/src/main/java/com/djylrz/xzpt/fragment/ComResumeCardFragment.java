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
import com.djylrz.xzpt.utils.ComResumeDeliveryRecordAdapter;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.RecruitmentAdapter;
import com.djylrz.xzpt.vo.ResumeDeliveryRecordVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressLint("ValidFragment")
public class ComResumeCardFragment extends Fragment {
    private String mTitle;
    private List<ResumeDeliveryRecordVO> resumeDeliveryRecordVOList = new ArrayList<ResumeDeliveryRecordVO>();
    private int type = 9999;
    private ComResumeDeliveryRecordAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private static final String TAG = "ComResumeCardFragment";
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private RequestQueue requestQueue;
    private long limitNum = 9999;

    public static ComResumeCardFragment getInstance(String title) {
        ComResumeCardFragment sf = new ComResumeCardFragment();
        sf.mTitle = title;
        if(title.equals("已拒绝")){
            sf.type = 0;
        }else if(title.equals("已通过")){
            sf.type = 1;
        }else if(title.equals("面试中")){
            sf.type = 2;
        }else if(title.equals("待审核")){
            sf.type = 3;
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
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); //把上下文context作为参数传递进去

        //加载数据
        initRecruitments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new ComResumeDeliveryRecordAdapter(resumeDeliveryRecordVOList,type,getContext());
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(loadMoreWrapper);
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                resumeDeliveryRecordVOList.clear();
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
                if (resumeDeliveryRecordVOList.size() < limitNum) {
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
        String url = PostParameterName.POST_URL_COMPANY_GET_DELIVERT_RECORD + token ;
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
                            Type jsonType = new TypeToken<TempResponseData<TempResponseResumeData<List<ResumeDeliveryRecordVO>> >>() {}.getType();
                            final TempResponseData<TempResponseResumeData<List<ResumeDeliveryRecordVO>> > postResult = gson.fromJson(response.toString(), jsonType);
                            Log.d(TAG, "onResponse: "+postResult.getResultCode());
                            if(postResult.getResultCode().equals(200)){
                                TempResponseResumeData<List<ResumeDeliveryRecordVO>>  resultObject = postResult.getResultObject();
                                List<ResumeDeliveryRecordVO> resumeDeliveryRecordVOS = resultObject.getContentList();
                                for (int i = 0; i < resumeDeliveryRecordVOS.size(); ++i) {
                                    int resumeRecordType = 0;
                                    switch ((int)resumeDeliveryRecordVOS.get(i).getDeliveryStatus()){
                                        case -1:
                                            resumeRecordType = 0;
                                            break;
                                        case 0:
                                            resumeRecordType = -1;
                                            break;
                                        case 1:
                                            resumeRecordType = 3;
                                            break;
                                        case 2:
                                            resumeRecordType = 3;
                                            break;
                                        case 3:
                                            resumeRecordType = 2;
                                            break;
                                        case 4:
                                            resumeRecordType = 2;
                                            break;
                                        case 5:
                                            resumeRecordType = 2;
                                            break;
                                        case 6:
                                            resumeRecordType = 2;
                                            break;
                                        case 7:
                                            resumeRecordType = 1;
                                            break;
                                        default:
                                            resumeRecordType = -1;
                                            break;
                                    }
                                    Log.d(TAG, "onResponse: 当前的type为"+ resumeRecordType);
                                    if(type == resumeRecordType){
                                        Log.d(TAG, "onResponse: 进入if，当前的type为"+ resumeRecordType);
                                        resumeDeliveryRecordVOList.add(resumeDeliveryRecordVOS.get(i));
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
                                        limitNum = resumeDeliveryRecordVOList.size();
                                    }
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
//        for(int i = 0; i< 20 ; ++i){
//            ResumeDeliveryRecordVO test1 = new ResumeDeliveryRecordVO("王铭君","待就业六人组PM","福州大学","软件工程",5);
//            resumeDeliveryRecordVOList.add(test1);
//        }
    }
}
class TempResponseResumeData<T> {
    private Integer currentPage;
    private Integer numOfPage;
    private Integer pageSize;
    private T contentList;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(Integer numOfPage) {
        this.numOfPage = numOfPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public T getContentList() {
        return contentList;
    }

    public void setContentList(T contentList) {
        this.contentList = contentList;
    }
}