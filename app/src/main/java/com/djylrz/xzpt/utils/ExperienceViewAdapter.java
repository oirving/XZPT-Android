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
import com.djylrz.xzpt.activityStudent.EditPracticeActivity;
import com.djylrz.xzpt.activityStudent.JobIntentionActivity;

import java.util.List;
public class ExperienceViewAdapter extends RecyclerView.Adapter<ExperienceViewAdapter.ViewHolder> {

    private List<ExperienceViewItem> experienceViewItems;

    public ExperienceViewAdapter(List<ExperienceViewItem> experienceViewItems) {
        this.experienceViewItems = experienceViewItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View experienceViewListView;
        TextView companyName;
        TextView position;
        ImageView next;
        public ViewHolder(View v) {
            super(v);
            experienceViewListView = v;
            companyName = (TextView)v.findViewById(R.id.company_name);
            position = (TextView)v.findViewById(R.id.position);
            next = (ImageView)v.findViewById(R.id.next);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.experience_view_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.experienceViewListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ExperienceViewItem experienceViewItem = experienceViewItems.get(position);
                //todo 跳转到对应的界面并填入信息 ->小榕
                Intent intent = new Intent(v.getContext(), EditPracticeActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        ExperienceViewItem experienceViewItem = experienceViewItems.get(position);
        holder.companyName.setText(experienceViewItem.getCompanyName());
        holder.position.setText(experienceViewItem.getPosition());
        holder.next.setImageResource(experienceViewItem.getNext());
    }

    @Override
    public int getItemCount() {
        return experienceViewItems.size();
    }

}