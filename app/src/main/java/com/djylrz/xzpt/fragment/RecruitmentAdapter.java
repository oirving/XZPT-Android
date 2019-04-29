package com.djylrz.xzpt.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;


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

    public RecruitmentAdapter(List<Recruitment> recruitmentList,int type) {
        this.mRecruitments = recruitmentList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recruitment_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.recruitmentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Recruitment recruitment = mRecruitments.get(position);
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
                    if (type == 0) {
                        Toast.makeText(v.getContext(), "you clicked end button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                    } else if (type == 1) {
                        Toast.makeText(v.getContext(), "you clicked delete button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "you clicked  button " + recruitment.getJobName(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            return holder;
        }else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }else {
            Recruitment recruitment = mRecruitments.get(position);
            holder.recruitmentName.setText(recruitment.getJobName());
            holder.recruitmentSalary.setText(recruitment.getSalary());
            holder.recruitmentCompany.setText(recruitment.getCompanyId());
            holder.recruitmentLocation.setText(recruitment.getLocation());
            holder.recruitmentDegree.setText(recruitment.getDegree());
            holder.recruitmentWorkTime.setText(recruitment.getWorkTime() + "");

            if (type == 0) {
                holder.editRecruitment.setText("编辑岗位");
                holder.opRecruitment.setText("结束招聘");
            } else if (type == 1) {
                holder.editRecruitment.setText("编辑岗位");
                holder.opRecruitment.setText("删除岗位");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRecruitments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private class FootViewHolder extends ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}