package com.djylrz.xzpt.fragmentStudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.ResumeModelHistoryActivity;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.ResumeTemplate;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentResume extends Fragment {
    private static final String TAG = "FragmentResume";
    private List<ResumeModelItem> resumeModelItemList = new ArrayList<>();
    private ResumeModelListAdapter resumeModelListAdapter;
    private Toolbar toolbar;

    private RecyclerView recyclerView;


    /**
     * Fragment中初始化Toolbar
     * @param toolbar
     * @param title 标题
     * @param isDisplayHomeAsUp 是否显示返回箭头
     */
    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2_resume,container,false);
        initResumeModel();
        toolbar = (Toolbar) view.findViewById(R.id.resume_toolbar);
        initToolbar(toolbar,"",false);
        setHasOptionsMenu(true);
        resumeModelListAdapter = new ResumeModelListAdapter(resumeModelItemList);
        recyclerView = (RecyclerView) view.findViewById(R.id.resume_model_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resumeModelListAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resume_history_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Toast.makeText(getContext(),"menu",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ResumeModelHistoryActivity.class);
                startActivity(intent);
        return  true;
    }

    public void initResumeModel() {
        /*for(int i =0;i<20;i++) {
            ResumeModelItem resumeModelItem = new ResumeModelItem(R.drawable.resumemodel2);
            resumeModelItemList.add(resumeModelItem);
        }*/
        //分页
        PageData pageData = new PageData();
        pageData.setCurrentPage(1);
        pageData.setPageSize(10);

        //查询简历模版并显示
        VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
        Log.d(TAG, "initResumeModel: " + PostParameterName.POST_URL_GET_RESUME_TEMPLATE);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    PostParameterName.POST_URL_GET_RESUME_TEMPLATE,
                    new JSONObject(new Gson().toJson(pageData)),
                    new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回了"+response.toString());
                            try {
                                switch(response.getString(PostParameterName.RESPOND_RESULTCODE)){
                                    case "200":{

                                        JSONArray jsonArray = response.getJSONArray(PostParameterName.RESPOND_RESULTOBJECT);
                                        List<ResumeTemplate> resumeTemplateList = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<ResumeTemplate>>(){}.getType());
                                        for (ResumeTemplate resumeTemplate:resumeTemplateList){
                                            resumeModelItemList.add(new ResumeModelItem(resumeTemplate));
                                        }
                                        resumeModelListAdapter = new ResumeModelListAdapter(resumeModelItemList);
                                        recyclerView.setAdapter(resumeModelListAdapter);
                                        break;
                                    }default:{
                                        Log.d(TAG, "onResponse: 获取模版失败");
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

        VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());//获取requestQueue
        VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
