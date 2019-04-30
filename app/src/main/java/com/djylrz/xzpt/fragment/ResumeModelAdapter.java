package com.djylrz.xzpt.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.ResumeItem;

import java.util.List;

public class ResumeModelAdapter extends RecyclerView.Adapter<ResumeModelAdapter.ViewHolder> {

    private List<ResumeItem> mResumeList;


    public ResumeModelAdapter(List<ResumeItem> resumeList) {
        mResumeList = resumeList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View resumeListView;
        Button resumeState;
        ImageView resumeEdit;
        TextView resumePosition;
        TextView resumeUserName;
        TextView resumeTime;

        public ViewHolder(View v) {
            super(v);
            resumeListView = v;
            resumeState = (Button) v.findViewById(R.id.resume_state_button);
            resumeEdit = (ImageView) v.findViewById(R.id.resume_edit_imageview);
            resumePosition = (TextView) v.findViewById(R.id.resume_position_textview);
            resumeUserName = (TextView) v.findViewById(R.id.resume_username_textview);
            resumeTime = (TextView) v.findViewById(R.id.resume_time_textview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_items,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.resumeListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResumeItem resumeItem = mResumeList.get(position);
                Toast.makeText(v.getContext(),"clicked" ,Toast.LENGTH_SHORT).show();
                switch (position) {

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumeItem resumeItem = mResumeList.get(position);
        holder.resumeState.setText(resumeItem.getState());
        holder.resumeTime.setText(resumeItem.getTime());
        holder.resumePosition.setText(resumeItem.getPosition());
        holder.resumeUserName.setText(resumeItem.getUserName());
        holder.resumeEdit.setImageResource(resumeItem.getEditImage());
    }

    @Override
    public int getItemCount() {
        return mResumeList.size();
    }



}