package com.djylrz.xzpt.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.Activity.ComRecruitmentDetailActivity;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Recruitment;

import java.util.List;

/**
 * @program: XZPT-Android
 * @description: 招聘岗位适配器
 * @author: mingjun
 * @create: 2019-04-28 01:00
 */
public class RecruitmentAdapter extends RecyclerView.Adapter<RecruitmentAdapter.ViewHolder> {

    private List<Recruitment> mRecruitments;
    private int type;
    private Context context;


    static class ViewHolder extends RecyclerView.ViewHolder{
        View recruitmentView;
        TextView recruitmentName;
        TextView recruitmentSalary;
        TextView recruitmentCompany;
        TextView recruitmentLocation;
        TextView recruitmentDegree;
        TextView recruitmentWorkTime;
        Button editRecruitment;
        Button opRecruitment;
        public ViewHolder(View view){
            super(view);
            recruitmentView = view;
            recruitmentName = (TextView) view.findViewById(R.id.recruitment_name);
            recruitmentSalary = (TextView) view.findViewById(R.id.recruitment_salary);
            recruitmentCompany = (TextView) view.findViewById(R.id.recruitment_company);
            recruitmentLocation = (TextView) view.findViewById(R.id.recruitment_location);
            recruitmentDegree = (TextView) view.findViewById(R.id.recruitment_degree);
            recruitmentWorkTime = (TextView) view.findViewById(R.id.recruitment_work_time);
            editRecruitment = (Button)view.findViewById(R.id.recruitment_edit);
            opRecruitment = (Button)view.findViewById(R.id.recruitment_op);
        }
    }

    public RecruitmentAdapter(List<Recruitment> mRecruitments, int type, Context context) {
        this.mRecruitments = mRecruitments;
        this.type = type;
        this.context = context;
    }

    public RecruitmentAdapter(List<Recruitment> recruitmentList, int type) {
        this.mRecruitments = recruitmentList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recruitment_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.recruitmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Recruitment recruitment = mRecruitments.get(position);
                //跳转到详情页
                Intent intent = new Intent(context, ComRecruitmentDetailActivity.class);
                intent.putExtra("recruitment",recruitment);
                context.startActivity(intent);
                Toast.makeText(v.getContext(), "you clicked view " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.editRecruitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Recruitment recruitment = mRecruitments.get(position);
                Toast.makeText(v.getContext(), "you clicked edit button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.opRecruitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Recruitment recruitment = mRecruitments.get(position);
                if(type == 0){
                    Toast.makeText(v.getContext(), "you clicked end button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                }else if(type == 1){
                    Toast.makeText(v.getContext(), "you clicked delete button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(), "you clicked  button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                }

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
        switch (Integer.parseInt(recruitment.getWorkTime()+"")){
            case 1:
                holder.recruitmentWorkTime.setText("955");
                break;
            case 2:
                holder.recruitmentWorkTime.setText("965");
                break;
            case 3:
                holder.recruitmentWorkTime.setText("956");
                break;
            case 4:
                holder.recruitmentWorkTime.setText("996");
                break;
            default:
                holder.recruitmentWorkTime.setText("955");
                break;
        }

        if(type == 0){
            holder.editRecruitment.setText("编辑岗位");
            holder.opRecruitment.setText("结束招聘");
        }else if(type == 1){
            holder.editRecruitment.setText("编辑岗位");
            holder.opRecruitment.setText("删除岗位");
        }
    }

    @Override
    public int getItemCount() {
        return mRecruitments.size();
    }
}