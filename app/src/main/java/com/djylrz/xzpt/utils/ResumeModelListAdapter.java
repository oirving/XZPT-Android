package com.djylrz.xzpt.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.djylrz.xzpt.R;

import java.util.List;

public class ResumeModelListAdapter extends RecyclerView.Adapter<ResumeModelListAdapter.ViewHolder> {

    private List<ResumeModelItem> mResumeModelList;


    public ResumeModelListAdapter(List<ResumeModelItem> resumeModelList) {
        mResumeModelList = resumeModelList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View resumeModelListView;
        ImageView resumeModel;

        public ViewHolder(View v) {
            super(v);
            resumeModelListView = v;
            resumeModel = (ImageView) v.findViewById(R.id.resume_preview_imageview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resume_model_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.resumeModelListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResumeModelItem resumeModelItem = mResumeModelList.get(position);
                Toast.makeText(v.getContext(),"clicked" ,Toast.LENGTH_SHORT).show();
                switch (position) {
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumeModelItem resumeModelItem = mResumeModelList.get(position);
        holder.resumeModel.setImageResource(resumeModelItem.getResumeModel());
    }

    @Override
    public int getItemCount() {
        return mResumeModelList.size();
    }



}