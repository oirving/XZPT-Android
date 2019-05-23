package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.JobIntentionActivity;

import java.util.List;
public class JobIntentionViewAdapter extends RecyclerView.Adapter<JobIntentionViewAdapter.ViewHolder> {

    private List<JobIntentViewItem> jobIntentViewItems;

    public JobIntentionViewAdapter(List<JobIntentViewItem> jobIntentViewItems) {
        this.jobIntentViewItems = jobIntentViewItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View jobIntentionViewListView;
        TextView jobName;
        TextView salary;
        TextView jobLocation;
        TextView jobIndustry;
        ImageView next;
        public ViewHolder(View v) {
            super(v);
            jobIntentionViewListView = v;
            jobName = (TextView)v.findViewById(R.id.job_name);
            salary = (TextView)v.findViewById(R.id.salary);
            jobLocation = (TextView)v.findViewById(R.id.job_location);
            jobIndustry = (TextView)v.findViewById(R.id.job_industry);
            next = (ImageView)v.findViewById(R.id.next);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobintention_view_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.jobIntentionViewListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                JobIntentViewItem jobIntentViewItem = jobIntentViewItems.get(position);
                //todo 跳转到相应的jobIntentionActivity,并填入对应数据
                Intent intent = new Intent(v.getContext(), JobIntentionActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        JobIntentViewItem jobIntentViewItem = jobIntentViewItems.get(position);
        holder.salary.setText(jobIntentViewItem.getBasicSalary()+"k - "+jobIntentViewItem.getTopSalary()+"K");
        holder.next.setImageResource(jobIntentViewItem.getNext());
        holder.jobIndustry.setText(jobIntentViewItem.getJobIndustry());
        holder.jobLocation.setText(jobIntentViewItem.getJobLocation());
        holder.jobName.setText(jobIntentViewItem.getJobName());
    }

    @Override
    public int getItemCount() {
        return jobIntentViewItems.size();
    }

}