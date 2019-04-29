package com.djylrz.xzpt.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;

import java.util.List;

/**
 * @author oirving
 * 招聘日历适配器
 */


public class RecruitmentDateAdapter extends RecyclerView.Adapter<RecruitmentDateAdapter.ViewHolder> {

    private List<RecruitmentDateItem> mRecruitmentDateItem;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView recruitmentTitle;//招聘会的标题
        TextView recruitmentAddress;//招聘会地址
        TextView recruitmentDateTime;//招聘会时间

        public ViewHolder(View view) {

            super(view);
            recruitmentTitle = (TextView) view.findViewById(R.id.recruitment_title_textview);
            recruitmentAddress = (TextView) view.findViewById(R.id.recruitment_address_textview);
            recruitmentDateTime = (TextView) view.findViewById(R.id.recruitment_datetime_textview);
        }
    }

    public RecruitmentDateAdapter (List<RecruitmentDateItem> recruitmentDateItem){
        mRecruitmentDateItem = recruitmentDateItem;
    }
    @NonNull
    @Override
    public RecruitmentDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recruitment_date_item,viewGroup,false);
        ViewHolder holder = new RecruitmentDateAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder  viewHolder, int position) {
        RecruitmentDateItem recruitmentDateItem = mRecruitmentDateItem.get(position);
        viewHolder.recruitmentDateTime.setText(recruitmentDateItem.getDateTime());
        viewHolder.recruitmentAddress.setText(recruitmentDateItem.getAddress());
        viewHolder.recruitmentTitle.setText(recruitmentDateItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return mRecruitmentDateItem.size();
    }
}
