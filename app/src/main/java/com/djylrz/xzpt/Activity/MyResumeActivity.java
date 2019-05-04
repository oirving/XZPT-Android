package com.djylrz.xzpt.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.*;
import com.djylrz.xzpt.fragment.RecommendCardFragment;
import com.djylrz.xzpt.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyResumeActivity extends AppCompatActivity {

    private static final String TAG = "MyResumeActivity";

    private List<ResumeItem>mResumeList = new ArrayList<>();
    private List<Resume> resumeList = new ArrayList<>();
    private String[] state;//简历状态
    private String[] position;//申请职位
    private String userName;//用户名称
    private String[] time;//申请时间
    private int numOfResume;//简历份数

    private RecyclerView recyclerView;
    private ResumeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resume);
        //todo 从系统读取简历基本状态——to小榕
        state = new String[]{"通过","未通过","待审核","asdjha"};//简历状态
        position = new String[]{};//申请职位
        userName = new String();//用户名
        time = new String[]{};//申请时间
        numOfResume=10;//简历份数！！！
        initResumeList();
        recyclerView = (RecyclerView)findViewById(R.id.myresume_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ResumeListAdapter(mResumeList);
        recyclerView.setAdapter(adapter);

    }

   //todo：获取所有的简历信息——》小榕
    private void initResumeList() {
        /*for(int i=0;i<numOfResume;i++) {
            //信息填入，上面的参数填好就不用管这里了，没填好之前点击会崩
            ResumeItem resumeItem = new ResumeItem("通过","董事局主席","小李","2019-4-21");
            mResumeList.add(resumeItem);
        }*/
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
                                            Gson gson = builder.create();

                                            //解析pageData
                                            Type jsonType = new TypeToken<PageData<Resume>>() {}.getType();
                                            final PageData<Resume> recruitmentPageData = gson.fromJson(pageDataResultObject.toString(),jsonType);

                                            //获取到ResumeList
                                            resumeList = recruitmentPageData.getContentList();
                                            Log.d(TAG, "onResponse: "+resumeList.size());
                                            mResumeList.clear();
                                            for (Resume resume : resumeList){
                                                //todo 简历表无创建简历时间，无岗位意向（求职意向中有）
                                                ResumeItem resumeItem = new ResumeItem(Constants.RESUME_STATE[(int)resume.getResumeStatus()+1],
                                                        resume.getExpectWork(),
                                                        resume.getUserName(),
                                                        String.valueOf(resume.getResumeId()));
                                                mResumeList.add(resumeItem);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //todo 处理获得的所有简历
                                        adapter.setmResumeList(mResumeList);
                                        adapter.setResumeList(resumeList);
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
}
