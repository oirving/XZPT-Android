package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.IntentResumeFileActivity;
import com.djylrz.xzpt.activityStudent.MyResumePreviewActivity;

import java.util.List;

public class DeliveryRecordAdapter extends RecyclerView.Adapter<DeliveryRecordAdapter.ViewHolder> {
    private List<DeliveryRecordItem> deliveryRecordItems;
    public DeliveryRecordAdapter(List<DeliveryRecordItem> deliveryRecordItems) {
        deliveryRecordItems = deliveryRecordItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View deliveryRecordView;
        ImageView delete;
        TextView resumeState;
        TextView companyName;
        TextView jobName;
        TextView username;

        public ViewHolder(View v) {
            super(v);
            deliveryRecordView = v;
            delete = (ImageView)v.findViewById(R.id.delete_resume);
            resumeState = (TextView)v.findViewById(R.id.resume_state_textview);
            companyName = (TextView)v.findViewById(R.id.company_name);
            jobName = (TextView)v.findViewById(R.id.job_name);
            username = (TextView)v.findViewById(R.id.resume_username_textview);
        }
    }

    @NonNull
    @Override
    public DeliveryRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_items,parent,false);
        final DeliveryRecordAdapter.ViewHolder holder = new DeliveryRecordAdapter.ViewHolder(v);
        holder.deliveryRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DeliveryRecordItem deliveryRecordItem = deliveryRecordItems.get(position);
                Intent intent = new Intent(v.getContext(), MyResumePreviewActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            //todo 补充删除事件 ->小榕
            @Override
            public void onClick(View v) {
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeliveryRecordItem deliveryRecordItem = deliveryRecordItems.get(i);
        viewHolder.username.setText(deliveryRecordItem.getUserName());
        viewHolder.jobName.setText(deliveryRecordItem.getJobName());
        viewHolder.companyName.setText(deliveryRecordItem.getCompanyName());
        viewHolder.resumeState.setText(deliveryRecordItem.getState());
        viewHolder.delete.setImageResource(deliveryRecordItem.getDelete());
    }

    @Override
    public int getItemCount() {
        return deliveryRecordItems.size();
    }
}
