package com.djylrz.xzpt.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.activity.student.ResumeModelDetailsActivity;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Resume;

import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.djylrz.xzpt.utils.PostParameterName.CHOOSE_RESUME_TO_DELIVER;

public class ResumeListAdapter extends RecyclerView.Adapter<ResumeListAdapter.ViewHolder> {
    private static final String TAG = "ResumeListAdapter";

    public List<DeliveryRecordItem> getmResumeList() {
        return mResumeList;
    }

    public void setmResumeList(List<DeliveryRecordItem> mResumeList) {
        this.mResumeList = mResumeList;
    }

    private List<DeliveryRecordItem> mResumeList;

    public List<Resume> getResumeList() {
        return resumeList;
    }

    public void setResumeList(List<Resume> resumeList) {
        this.resumeList = resumeList;
    }

    private List<Resume> resumeList;//完整的简历信息

    private boolean forDeliver = false;

    public boolean isForDeliver() {
        return forDeliver;
    }

    public void setForDeliver(boolean forDeliver) {
        this.forDeliver = forDeliver;
    }

    public ResumeListAdapter(List<DeliveryRecordItem> mResumeList) {
        this.mResumeList = mResumeList;
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
            delete = (ImageView)v.findViewById(R.id.delete_resume);
            resumeState = (TextView)v.findViewById(R.id.resume_state_textview);
            companyName = (TextView)v.findViewById(R.id.company_name);
            jobName = (TextView)v.findViewById(R.id.job_name);
            username = (TextView)v.findViewById(R.id.resume_username_textview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_items,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.deliveryRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DeliveryRecordItem resumeItem = mResumeList.get(position);
                RxToast.info("");
                //todo 我的简历点击后跳转到具体的简历页面 ->小榕
                Intent intent = new Intent(v.getContext(), ResumeModelDetailsActivity.class);
                intent.putExtra("resume",resumeList.get(position));//传递简历信息
                intent.putExtra("resumeID",resumeList.get(position).getResumeId());
                if (forDeliver){
                    ((Activity)v.getContext()).setResult(CHOOSE_RESUME_TO_DELIVER,intent);//选择简历用于投递
                    ((Activity)v.getContext()).finish();//选择并投递

                }else{
                    v.getContext().startActivity(intent);
                }


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int position = holder.getAdapterPosition();
                DeliveryRecordItem resumeItem = mResumeList.get(position);
                RxToast.info("");
                //todo 删除简历 ->小榕
                long resumeID = resumeList.get(position).getResumeId();

                String token  =  v.getContext().getSharedPreferences("token",0).getString(PostParameterName.STUDENT_TOKEN,null);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        PostParameterName.POST_URL_DELETE_RESUME+token+
                            "&"+PostParameterName.REQUEST_RESUME_ID+"="+resumeID,
                            new JSONObject(),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: 返回"+response.toString());
                                    try {
                                        if ("200".equals(response.getString(PostParameterName.RESPOND_RESULTCODE))){
                                            Log.d(TAG, "onResponse: 删除简历成功");
                                            mResumeList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }});
                    VolleyNetUtil.getInstance().setRequestQueue(v.getContext());
                    VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DeliveryRecordItem deliveryRecordItem = mResumeList.get(i);
        viewHolder.username.setText(deliveryRecordItem.getUserName());
        viewHolder.jobName.setText(deliveryRecordItem.getJobName());
        viewHolder.companyName.setText(deliveryRecordItem.getCompanyName());
        viewHolder.resumeState.setText(deliveryRecordItem.getState());
        viewHolder.delete.setImageResource(deliveryRecordItem.getDelete());
    }

    @Override
    public int getItemCount() {
        return mResumeList.size();
    }



}