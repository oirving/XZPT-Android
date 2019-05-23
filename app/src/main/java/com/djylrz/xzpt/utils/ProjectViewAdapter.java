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
import com.djylrz.xzpt.activityStudent.EditProjectActivity;

import java.util.List;
public class ProjectViewAdapter extends RecyclerView.Adapter<ProjectViewAdapter.ViewHolder> {

    private List<ProjectItem> projectViewItemList;

    public ProjectViewAdapter(List<ProjectItem> projectViewItemList) {
        this.projectViewItemList = projectViewItemList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View projectViewListView;
        TextView projectName;
        ImageView next;
        public ViewHolder(View v) {
            super(v);
            projectViewListView = v;
            projectName = (TextView)v.findViewById(R.id.project_name);
            next = (ImageView)v.findViewById(R.id.next);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_view_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.projectViewListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ProjectItem projectViewItem = projectViewItemList.get(position);
                //todo 跳转到相应的EditProjectActivity,填入对应数据 ->小榕
                Intent intent = new Intent(v.getContext(), EditProjectActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ProjectItem projectViewItem = projectViewItemList.get(position);
        holder.projectName.setText(projectViewItem.getProjectName());
        holder.next.setImageResource(projectViewItem.getNext());
    }

    @Override
    public int getItemCount() {
        return projectViewItemList.size();
    }

}