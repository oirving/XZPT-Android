package com.djylrz.xzpt.activityStudent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.DeliveryRecordAdapter;
import com.djylrz.xzpt.utils.DeliveryRecordItem;
import com.djylrz.xzpt.utils.ResumeListAdapter;
import com.djylrz.xzpt.utils.ResumeModelItem;
import com.djylrz.xzpt.utils.ResumeModelListAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRecordActivity extends AppCompatActivity {

    private List<DeliveryRecordItem> deliveryRecordItemList = new ArrayList<>();
    private ResumeListAdapter deliveryRecordAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_record);
        initDeliveryRecord();
        deliveryRecordAdapter = new ResumeListAdapter(deliveryRecordItemList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.resume_record_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deliveryRecordAdapter);
    }

    //todo 原来写的，删掉了...
    public void initDeliveryRecord() {
        for(int i =0;i<3;i++) {
            DeliveryRecordItem deliveryRecordItem = new DeliveryRecordItem("通过","阿里巴巴","算法工程师","大O");
            deliveryRecordItemList.add(deliveryRecordItem);
        }
    }
}
