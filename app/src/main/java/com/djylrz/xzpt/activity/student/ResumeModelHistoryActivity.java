package com.djylrz.xzpt.activity.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.ResumeRecord;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
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

public class ResumeModelHistoryActivity extends AppCompatActivity {
    private static final String TAG = "ResumeHistoryActivity";
    private List<ResumeModelItem> resumeModelIHistorytemList = new ArrayList<>();
    private ResumeModelListAdapter resumeModelItemHistoryListAdapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_model_history);
        initResumeModel();
        toolbar = (Toolbar) findViewById(R.id.resume_history_toolbar);
        //设置标题栏
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resumeModelItemHistoryListAdapter = new ResumeModelListAdapter(resumeModelIHistorytemList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.resume_model_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resumeModelItemHistoryListAdapter);
    }

    //todo 把历史记录中简历的缩略图放进来，图片传输有待更改，目前只放了本地R类的文件
    public void initResumeModel() {
        /*for(int i =0;i<3;i++) {
            ResumeModelItem resumeModelItem = new ResumeModelItem(R.drawable.resumemodel2);
            resumeModelIHistorytemList.add(resumeModelItem);
        }*/
        //todo 没有把简历模版resumeTemplate赋给ResumeModeItem
        //todo 需要从后端获取导出的简历的缩略图和导出的简历的下载链接
        //todo 导出简历接口接收模版文件名，返回生成简历word文件路径
        //todo 获取简历模版导出历史记录——后端接口无

        //todo 历史记录接口接收用户ID，返回已经生成的所有简历的下载链接和简历所应用的模版的缩略图链接，查看的时候需要重新下载
        //todo 历史记录查看所有本地已经下载的简历，因为导出过的简历都会下载到本地，因此查看无需下载

        //分页
        PageData pageData = new PageData();
        pageData.setCurrentPage(1);
        pageData.setPageSize(10);

        //查询简历模版并显示
        String token  = getSharedPreferences("token",0).getString(PostParameterName.STUDENT_TOKEN,null);

        String url = PostParameterName.POST_URL_GET_GENERATED_RESUME_HISTORY+token;//todo 若有参数，拼接URL
        Log.d(TAG, "initResumeModel: "+url);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url,
                    new JSONObject(new Gson().toJson(pageData)),
                    new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回了"+response.toString());
                            try {
                                switch(response.getString(PostParameterName.RESPOND_RESULTCODE)){
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

                                        //todo 替换T
                                        Type jsonType = new TypeToken<PageData<ResumeRecord>>() {}.getType();
                                        final PageData<ResumeRecord> TPageData = gson.fromJson(pageDataResultObject.toString(),jsonType);

                                        List<ResumeRecord> resumeRecordList = TPageData.getContentList();
                                        Log.d(TAG, "onResponse: "+resumeRecordList.size());
                                        resumeModelIHistorytemList.clear();
                                        for (ResumeRecord resumeRecord : resumeRecordList){
                                            ResumeModelItem resumeModelItem = new ResumeModelItem(resumeRecord);//todo ResumeModelItem添加T的成员t
                                            resumeModelIHistorytemList.add(resumeModelItem);//todo ResumeModelItem需要添加新的构造函数
                                        }
                                        resumeModelItemHistoryListAdapter.notifyDataSetChanged();
                                        break;
                                    }default:{
                                        Log.d(TAG, "onResponse: 获取生成简历历史记录失败");
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

            VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
