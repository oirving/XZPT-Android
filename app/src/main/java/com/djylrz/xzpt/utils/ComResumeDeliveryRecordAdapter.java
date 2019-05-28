package com.djylrz.xzpt.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.activityCompany.ComResumeDeliveryRecordDetailActivity;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.ResumeDelivery;
import com.djylrz.xzpt.vo.ResumeDeliveryRecordVO;

import java.util.List;
/**
 * @program: XZPT-Android
 * @description: 企业查看投递记录适配器
 * @author: mingjun
 * @create: 2019-04-28 01:00
 */
public class ComResumeDeliveryRecordAdapter extends RecyclerView.Adapter<ComResumeDeliveryRecordAdapter.ViewHolder> {
    private List<ResumeDelivery> mResumeDeliveryList;
    private int type;
    private Context context;
    private static final String TAG = "ComResumeDeliveryRecord";

    static class ViewHolder extends RecyclerView.ViewHolder{
        View resumeDeliveryRecordView;
        TextView resumeDeliveryRecordRecruitmentName;
        TextView resumeDeliveryRecordStatus;
        TextView resumeDeliveryRecordUserName;
        TextView resumeDeliveryRecordSchool;
        TextView resumeDeliveryRecordSpeciality;

        public ViewHolder(View view){
            super(view);
            resumeDeliveryRecordView = view;
            resumeDeliveryRecordRecruitmentName = (TextView)view.findViewById(R.id.tv_recruitment_record_name);
            resumeDeliveryRecordStatus = (TextView)view.findViewById(R.id.tv_recruitment_record_status);
            resumeDeliveryRecordUserName = (TextView)view.findViewById(R.id.tv_record_username);
            resumeDeliveryRecordSchool = (TextView)view.findViewById(R.id.tv_record_school);
            resumeDeliveryRecordSpeciality = (TextView)view.findViewById(R.id.tv_record_speciality);
        }
    }

    public ComResumeDeliveryRecordAdapter(List<ResumeDelivery> mRecruitments, int type, Context context) {
        this.mResumeDeliveryList = mRecruitments;
        this.type = type;
        this.context = context;
    }

    @NonNull
    @Override
    public ComResumeDeliveryRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + "正在渲染");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_delivery_record_item, parent, false);
        final ComResumeDeliveryRecordAdapter.ViewHolder holder = new ComResumeDeliveryRecordAdapter.ViewHolder(view);
        holder.resumeDeliveryRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResumeDelivery resumeDeliveryRecordVO = mResumeDeliveryList.get(position);
                //跳转到详情页
                Intent intent = new Intent(context, ComResumeDeliveryRecordDetailActivity.class);
                intent.putExtra("resumeDeliveryRecordVO",resumeDeliveryRecordVO);
                context.startActivity(intent);
                Toast.makeText(v.getContext(), "you clicked view " + resumeDeliveryRecordVO.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResumeDelivery resumeDeliveryRecordVO = mResumeDeliveryList.get(position);
        holder.resumeDeliveryRecordRecruitmentName.setText(resumeDeliveryRecordVO.getRecruitmentName());
        String resumeRecordType;
        switch ((int)resumeDeliveryRecordVO.getDeliveryStatus()){
            case -1:
                resumeRecordType = "已拒绝";
                break;
            case 1:
                resumeRecordType = "未查看";
                break;
            case 2:
                resumeRecordType = "已查看";
                break;
            case 3:
                resumeRecordType = "面试待安排";
                break;
            case 4:
                resumeRecordType = "一面";
                break;
            case 5:
                resumeRecordType = "二面";
                break;
            case 6:
                resumeRecordType = "终面";
                break;
            case 7:
                resumeRecordType = "已录用";
                break;
            default:
                resumeRecordType = "未审核";
                break;
        }
        holder.resumeDeliveryRecordStatus.setText(resumeRecordType);
        holder.resumeDeliveryRecordUserName.setText(resumeDeliveryRecordVO.getUserName());
        holder.resumeDeliveryRecordSchool.setText(resumeDeliveryRecordVO.getSchool());
        holder.resumeDeliveryRecordSpeciality.setText(resumeDeliveryRecordVO.getSpeciality());

    }

    @Override
    public int getItemCount() {
        return mResumeDeliveryList.size();
    }
}
