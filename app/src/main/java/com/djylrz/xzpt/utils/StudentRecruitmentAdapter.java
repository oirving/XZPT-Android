package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.Activity.RecruitmentDetailActivity;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;

import java.util.List;

/**
 * @program: XZPT-Android
 * @description: 招聘岗位适配器
 * @author: mingjun
 * @create: 2019-04-28 01:00
 */
public class StudentRecruitmentAdapter extends RecyclerView.Adapter<StudentRecruitmentAdapter.ViewHolder> {

    private List<Recruitment> mRecruitments;
    private int type;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View recruitmentView;
        TextView recruitmentName;
        TextView recruitmentSalary;
        TextView recruitmentCompany;
        TextView recruitmentLocation;
        TextView recruitmentDegree;
        TextView recruitmentWorkTime;
        public ViewHolder(View view){
            super(view);
            recruitmentView = view;
            recruitmentName = (TextView) view.findViewById(R.id.recruitment_name);
            recruitmentSalary = (TextView) view.findViewById(R.id.recruitment_salary);
            recruitmentCompany = (TextView) view.findViewById(R.id.recruitment_company);
            recruitmentLocation = (TextView) view.findViewById(R.id.recruitment_location);
            recruitmentDegree = (TextView) view.findViewById(R.id.recruitment_degree);
            recruitmentWorkTime = (TextView) view.findViewById(R.id.recruitment_work_time);
        }
    }

    public StudentRecruitmentAdapter(List<Recruitment> recruitmentList,int type) {
        this.mRecruitments = recruitmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recruitement_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.recruitmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Recruitment recruitment = mRecruitments.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                //todo 在招聘信息列表里点击后跳转的具体招聘信息页面 ->小榕
                Intent intent = new Intent(v.getContext(), RecruitmentDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recruitment recruitment = mRecruitments.get(position);
        holder.recruitmentName.setText(recruitment.getJobName());
        holder.recruitmentSalary.setText(recruitment.getSalary());
        holder.recruitmentCompany.setText(recruitment.getCompanyName());
        holder.recruitmentLocation.setText(recruitment.getLocation());
        holder.recruitmentDegree.setText(recruitment.getDegree());
        holder.recruitmentWorkTime.setText(recruitment.getWorkTime()+"");
    }

    @Override
    public int getItemCount() {
        return mRecruitments.size();
    }
}