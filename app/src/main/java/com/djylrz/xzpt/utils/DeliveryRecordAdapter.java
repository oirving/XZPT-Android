package com.djylrz.xzpt.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.DefaultMessagesActivity;

import java.util.List;

public class DeliveryRecordAdapter extends RecyclerView.Adapter<DeliveryRecordAdapter.ViewHolder> {
    private List<DeliveryRecordItem> deliveryRecordItems;

    public DeliveryRecordAdapter(List<DeliveryRecordItem> deliveryRecordItems) {

        this.deliveryRecordItems = deliveryRecordItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View deliveryRecordView;
        ImageView delete;
        TextView resumeState;
        TextView companyName;
        TextView jobName;
        TextView username;

        public ViewHolder(View v) {
            super(v);
            deliveryRecordView = v;
            delete = (ImageView) v.findViewById(R.id.delete_resume);
            resumeState = (TextView) v.findViewById(R.id.resume_state_textview);
            companyName = (TextView) v.findViewById(R.id.company_name);
            jobName = (TextView) v.findViewById(R.id.job_name);
            username = (TextView) v.findViewById(R.id.resume_username_textview);
        }
    }

    @NonNull
    @Override
    public DeliveryRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_items, parent, false);
        final DeliveryRecordAdapter.ViewHolder holder = new DeliveryRecordAdapter.ViewHolder(v);
        holder.deliveryRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 投递记录需要跳转到详细界面么？需要！
                /*
                int position = holder.getAdapterPosition();
                DeliveryRecordItem deliveryRecordItem = deliveryRecordItems.get(position);
                Intent intent = new Intent(v.getContext(), EditMyResumeActivity.class);
                intent.putExtra(Constants.INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME,Constants.EDIT_RESUME);
                intent.putExtra("resumeID",deliveryRecordItem.getResumeDelivery().getResumeId());
                v.getContext().startActivity(intent);*/
            }
        });
        holder.deliveryRecordView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("咨询").setMessage("是否向该公司发送私信咨询？")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = holder.getAdapterPosition();
                                DeliveryRecordItem deliveryRecordItem = deliveryRecordItems.get(position);
                                DefaultMessagesActivity.open(v.getContext(), deliveryRecordItem.getCompanyId(), deliveryRecordItem.getCompanyName(), "");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                return true;
            }
        });
        /*holder.delete.setOnClickListener(new View.AbstractOnClickListener() {
            //用户不需要删除投递记录
            @Override
            public void onClick(View v) {
                deliveryRecordItems.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeliveryRecordItem deliveryRecordItem = deliveryRecordItems.get(i);
        viewHolder.username.setText(deliveryRecordItem.getUserName());
        viewHolder.jobName.setText(deliveryRecordItem.getJobName());
        viewHolder.companyName.setText(deliveryRecordItem.getCompanyName());
        viewHolder.resumeState.setText(deliveryRecordItem.getState());
        viewHolder.delete.setImageResource(deliveryRecordItem.getDelete());
    }

    @Override
    public int getItemCount() {
        return deliveryRecordItems.size();
    }
}
