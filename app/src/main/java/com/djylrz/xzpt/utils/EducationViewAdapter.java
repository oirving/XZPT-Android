package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.JobIntentionActivity;
import com.djylrz.xzpt.activityStudent.MainActivity;
import com.djylrz.xzpt.activityStudent.PersonalInformation;

import java.util.List;

import static android.widget.ListPopupWindow.MATCH_PARENT;

public class EducationViewAdapter extends RecyclerView.Adapter<EducationViewAdapter.ViewHolder> {

    private List<EducationViewItem> educationtViewItems;

    public EducationViewAdapter(List<EducationViewItem> educationtViewItems) {
        this.educationtViewItems = educationtViewItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View educationViewListView;
        TextView schoolName;
        TextView degree;
        TextView time;
        ImageView next;
        public ViewHolder(View v) {
            super(v);
            educationViewListView = v;
            schoolName = (TextView)v.findViewById(R.id.school_name);
            time = (TextView)v.findViewById(R.id.education_time);
            degree = (TextView)v.findViewById(R.id.degree);
            next = (ImageView)v.findViewById(R.id.next);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.education_view_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.educationViewListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                EducationViewItem educationtViewItem = educationtViewItems.get(position);
                //todo 跳转到个人信息 ->小榕
                Intent intent = new Intent(v.getContext(), PersonalInformation.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        EducationViewItem educationtViewItem = educationtViewItems.get(position);
        holder.schoolName.setText(educationtViewItem.getSchoolName());
        holder.degree.setText(educationtViewItem.getDegree());
        holder.time.setText(educationtViewItem.getStartTime()+" - "+educationtViewItem.getEndTime());
        holder.next.setImageResource(educationtViewItem.getNext());
    }
    @Override
    public int getItemCount() {
        return educationtViewItems.size();
    }

}