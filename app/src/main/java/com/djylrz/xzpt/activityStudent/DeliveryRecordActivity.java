package com.djylrz.xzpt.activityStudent;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Resume;
import com.djylrz.xzpt.bean.ResumeDelivery;
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

public class DeliveryRecordActivity extends AppCompatActivity {
    private static final String TAG = "DeliveryRecordActivity";

    private List<DeliveryRecordItem> deliveryRecordItemList = new ArrayList<>();
    private DeliveryRecordAdapter deliveryRecordAdapter;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_record);
        toolbar = findViewById(R.id.resume_delivery_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deliveryRecordAdapter = new DeliveryRecordAdapter(deliveryRecordItemList);
        initDeliveryRecord();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.resume_record_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deliveryRecordAdapter);
    }

    //todo "投递记录"接口尚未实现
    public void initDeliveryRecord() {
        /*for(int i =0;i<3;i++) {
            DeliveryRecordItem deliveryRecordItem = new DeliveryRecordItem("通过","阿里巴巴","算法工程师","大O");
            deliveryRecordItemList.add(deliveryRecordItem);
        }*/

        SharedPreferences userToken = getSharedPreferences("token",0);
        String token = userToken.getString(PostParameterName.STUDENT_TOKEN,null);
        if (token != null){
            PageData pageData = new PageData();
            pageData.setCurrentPage(1);
            pageData.setPageSize(10);
            try {
                Log.d(TAG, "initDeliveryRecord: "+new Gson().toJson(pageData));
                Log.d(TAG, "initDeliveryRecord: "+PostParameterName.POST_URL_GET_DELIVER_RESUME_HISTORY+token);//todo 修改接口URL
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        PostParameterName.POST_URL_GET_DELIVER_RESUME_HISTORY+token,//todo 修改接口URL
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
                                            //todo 替换<ResumeDelivery> :投递记录的类
                                            Type jsonType = new TypeToken<PageData<ResumeDelivery>>() {}.getType();
                                            final PageData<ResumeDelivery> resumePageData = gson.fromJson(pageDataResultObject.toString(),jsonType);

                                            List<ResumeDelivery> list = resumePageData.getContentList();
                                            Log.d(TAG, "onResponse: "+list.size());
                                            deliveryRecordItemList.clear();
                                            for (ResumeDelivery t : list){
                                                DeliveryRecordItem deliveryRecordItem = new DeliveryRecordItem(t);
                                                deliveryRecordItemList.add(deliveryRecordItem);
                                            }
                                            deliveryRecordAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //处理空列表
                                        if (deliveryRecordItemList.size()==0){
                                            RxToast.info("无投递记录");
                                        }
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
            Log.d(TAG, "initDeliveryRecord: "+"没有获取到token");
        }
    }
}
