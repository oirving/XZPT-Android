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
import com.djylrz.xzpt.activityStudent.EditMyResumeActivity;

import java.util.List;

public class MyResumeAdapter extends RecyclerView.Adapter<MyResumeAdapter.ViewHolder> {

    private List<MyResumeItem> myResumeItems;
    private OnremoveListnner onremoveListnner;

    public MyResumeAdapter(List<MyResumeItem> myResumeItems) {
        this.myResumeItems = myResumeItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View myResumeListView;
        TextView jobName;
        TextView resumeState;
        TextView companyName;
        ImageView next;
        ImageView delete;
        public ViewHolder(View v) {
            super(v);
            myResumeListView = v;
            jobName = (TextView)v.findViewById(R.id.job_name);
            resumeState = (TextView)v.findViewById(R.id.resume_state);
            companyName = (TextView)v.findViewById(R.id.company_name);
            next = (ImageView)v.findViewById(R.id.next);
            delete = (ImageView) v.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.myResumeListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MyResumeItem myResumeItem = myResumeItems.get(position);
                //todo 跳转到BasicResumeActivity,填入对应该简历的信息 ->小榕
                Intent intent = new Intent(v.getContext(), EditMyResumeActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MyResumeItem myResumeItem = myResumeItems.get(position);
        holder.jobName.setText(myResumeItem.getJobName());
        holder.companyName.setText(myResumeItem.getCompanyName());
        holder.delete.setImageResource(myResumeItem.getDelete());
        holder.resumeState.setText(myResumeItem.getResumeState());
        switch (myResumeItem.getResumeState()) {
            case "通过" :
                holder.resumeState.setBackgroundColor(R.color.green);
                break;
            case "待处理":
                holder.resumeState.setBackgroundColor(R.color.yellow);
        }
        holder.next.setImageResource(myResumeItem.getNext());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长按跳出删除按钮
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            //删除的事件
            public void onClick(View v) {
                if (onremoveListnner!=null) {
                    onremoveListnner.ondelect(position);
                    //todo 在数据库删除对应的简历 ->小榕
                }
            }
        });
    }

    public interface OnremoveListnner{
        void  ondelect(int i);
    }

    public void setOnremoveListnner(OnremoveListnner onremoveListnner) {
        this.onremoveListnner = onremoveListnner;
    }

    @Override
    public int getItemCount() {
        return myResumeItems.size();
    }

}