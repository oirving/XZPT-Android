package com.djylrz.xzpt.activityStudent;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.*;
import com.djylrz.xzpt.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.djylrz.xzpt.utils.PostParameterName.CHOOSE_RESUME_TO_DELIVER;
import static com.vondear.rxtool.RxTool.getContext;


public class MyResumeActivity extends AppCompatActivity {

    private static final String TAG = "MyResumeActivity";

    private List<MyResumeItem> myResumeList = new ArrayList<>();
    private List<Resume> resumeList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MyResumeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume);
        //todo 从系统读取简历基本状态——to小榕
        //initResumeList();
        recyclerView = (RecyclerView)findViewById(R.id.resume_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyResumeAdapter(myResumeList);
        recyclerView.setAdapter(adapter);
        initPage();
        //删除item
        //todo 删除的事件在MyResumeAdapter.java里做
        adapter.setOnremoveListnner(new MyResumeAdapter.OnremoveListnner() {
            @Override
            public void ondelect(int i) {
                myResumeList.remove(i);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),""+i,Toast.LENGTH_SHORT).show();
            }
        });
    }

//   //todo：获取所有的简历信息——》小榕
//    private void initResumeList() {
//        SharedPreferences userToken = getSharedPreferences("token",0);
//        String token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
//        if (token != null){
//            PageData pageData = new PageData();
//            pageData.setCurrentPage(1);
//            pageData.setPageSize(10);
//            try {
//                Log.d(TAG, "initResumeList: post content: "+new Gson().toJson(pageData));
//                Log.d(TAG, "initResumeList: "+PostParameterName.POST_URL_GET_LIST_RESUME+token);
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                        PostParameterName.POST_URL_GET_LIST_RESUME+token,
//                        new JSONObject(new Gson().toJson(pageData)),
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d(TAG, "onResponse: 返回"+response.toString());
//                                try {
//                                    switch (response.getString(PostParameterName.RESPOND_RESULTCODE)){
//                                        case "200":{
//                                            JSONObject pageDataResultObject = response.getJSONObject("resultObject");
//
//                                            GsonBuilder builder = new GsonBuilder();
//                                            builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
//                                                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
//                                                    return new Timestamp(json.getAsJsonPrimitive().getAsLong());
//                                                }
//                                            });
//                                            Gson gson = builder.create();
//
//                                            //解析pageData
//                                            Type jsonType = new TypeToken<PageData<Resume>>() {}.getType();
//                                            final PageData<Resume> recruitmentPageData = gson.fromJson(pageDataResultObject.toString(),jsonType);
//
//                                            //获取到ResumeList
//                                            resumeList = recruitmentPageData.getContentList();
//                                            Log.d(TAG, "onResponse: "+resumeList.size());
//                                            myResumeList.clear();
//                                            for (Resume resume : resumeList){
//                                                //todo 简历表无创建简历时间，无岗位意向（求职意向中有）
//                                                MyResumeItem resumeItem = new MyResumeItem(
//                                                        resume.getUserName());
//                                                myResumeList.add(resumeItem);
//                                            }
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //todo 处理获得的所有简历
//                                        adapter.set
//                                        adapter.setResumeList(resumeList);
//                                        if (getIntent().getIntExtra("selectResume",0)==CHOOSE_RESUME_TO_DELIVER){//选择简历用于投体
//                                            adapter.setForDeliver(true);
//                                        }
//                                        recyclerView.setAdapter(adapter);
//                                    }
//                                });
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TAG Response failed", error.getMessage(), error);
//                    }});
//
//                VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
//                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }else{
//            Log.d(TAG, "initRecruitments: 没有获取到token");
//        }
//    }
    public void initPage(){
        //todo 填入简历列表Item的基本信息,按照以下格式填入 ->小榕
        for(int i=0;i<5;i++) {
            MyResumeItem myResumeItem = new MyResumeItem("算法工程师");
            myResumeList.add(myResumeItem);
        }
    }
}
