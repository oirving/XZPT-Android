package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import com.vondear.rxtool.view.RxToast;
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
    private ImageView addResume;
    private MyResumeAdapter adapter;
    private Toolbar toolbar;

    /**
     * Fragment中初始化Toolbar
     * @param toolbar
     * @param title 标题
     * @param isDisplayHomeAsUp 是否显示返回箭头
     */
    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
        AppCompatActivity appCompatActivity= this;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume);
        //从系统读取简历基本状态——to小榕
        //initResumeList();
        recyclerView = (RecyclerView)findViewById(R.id.resume_list);
        toolbar = findViewById(R.id.my_resume_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyResumeAdapter(myResumeList);
        recyclerView.setAdapter(adapter);
        addResume = (ImageView)findViewById(R.id.add_resume);
        addResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加一份简历，跳转到简历编辑页面,不可变的基本信息可以先填入 ->小榕
                Intent intent = new Intent(MyResumeActivity.this,EditMyResumeActivity.class);
                intent.putExtra(Constants.INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME,Constants.CREATE_RESUME);
                RxToast.info( Constants.CREATE_RESUME);
                startActivity(intent);
            }
        });
        initPage();
        //删除item
        //删除的事件在MyResumeAdapter.java里做
        adapter.setOnRemoveListener(new MyResumeAdapter.onRemoveListener() {
            @Override
            public void onDelete(int i) {
                myResumeList.remove(i);
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        initResumeList();
    }

    //获取所有的简历信息——》小榕
    private void initResumeList() {
        SharedPreferences userToken = getSharedPreferences("token",0);
        String token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        if (token != null){
            PageData pageData = new PageData();
            pageData.setCurrentPage(1);
            pageData.setPageSize(10);
            try {
                Log.d(TAG, "initResumeList: post content: "+new Gson().toJson(pageData));
                Log.d(TAG, "initResumeList: "+PostParameterName.POST_URL_GET_LIST_RESUME+token);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        PostParameterName.POST_URL_GET_LIST_RESUME+token,
                        new JSONObject(new Gson().toJson(pageData)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回"+response.toString());
                                try {
                                    switch (response.getString(PostParameterName.RESPOND_RESULTCODE)){
                                        case "200":{
                                            JSONObject pageDataResultObject = response.getJSONObject("resultObject");

                                            GsonBuilder builder = new GsonBuilder();
                                            builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                    return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                                }
                                            });
                                            Gson gson = builder
                                                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                                            //解析pageData
                                            Type jsonType = new TypeToken<PageData<Resume>>() {}.getType();
                                            final PageData<Resume> resumePageData = gson.fromJson(pageDataResultObject.toString(),jsonType);

                                            //获取到ResumeList
                                            resumeList = resumePageData.getContentList();
                                            Log.d(TAG, "onResponse: "+resumeList.size());
                                            myResumeList.clear();
                                            for (Resume resume : resumeList){
                                                //简历表无创建简历时间，无岗位意向（求职意向中有）
                                                MyResumeItem resumeItem = new MyResumeItem(
                                                        resume);
                                                //显示未投递的简历（已实现）
                                                if (((int)resume.getResumeStatus())==Constants.RESUME_STATE_NOT_DELIVERED){
                                                    myResumeList.add(resumeItem);
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //处理空列表
                                        if (myResumeList.size()==0){
                                            RxToast.info("无未投递简历");
                                        }
                                        //todo 处理选择投递的简历
                                        if (getIntent().getIntExtra("selectResume",0)==CHOOSE_RESUME_TO_DELIVER){//选择简历用于投体
                                            adapter.setForDeliver(true);
                                        }
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG Response failed", error.getMessage(), error);
                    }});

                VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Log.d(TAG, "initRecruitments: 没有获取到token");
        }
    }
    public void initPage(){
        initResumeList();
    }
}
