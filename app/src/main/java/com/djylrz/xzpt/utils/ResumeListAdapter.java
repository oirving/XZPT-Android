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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.djylrz.xzpt.Activity.MyResumeActivity;
import com.djylrz.xzpt.Activity.NewResumeActivity;
import com.djylrz.xzpt.Activity.ResumeModelDetailsActivity;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.Resume;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ResumeListAdapter extends RecyclerView.Adapter<ResumeListAdapter.ViewHolder> {
    private static final String TAG = "ResumeListAdapter";

    public List<ResumeItem> getmResumeList() {
        return mResumeList;
    }

    public void setmResumeList(List<ResumeItem> mResumeList) {
        this.mResumeList = mResumeList;
    }

    private List<ResumeItem> mResumeList;

    public List<Resume> getResumeList() {
        return resumeList;
    }

    public void setResumeList(List<Resume> resumeList) {
        this.resumeList = resumeList;
    }

    private List<Resume> resumeList;//完整的简历信息


    public ResumeListAdapter(List<ResumeItem> resumeList) {
        mResumeList = resumeList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View resumeListView;
        TextView resumeState;
        ImageView resumeEdit;
        TextView resumePosition;
        TextView resumeUserName;
        TextView resumeTime;
        ImageView deleteResume;

        public ViewHolder(View v) {
            super(v);
            resumeListView = v;
            resumeState = (TextView) v.findViewById(R.id.resume_state_textview);
            resumeEdit = (ImageView) v.findViewById(R.id.resume_edit_imageview);
            resumePosition = (TextView) v.findViewById(R.id.resume_position_textview);
            resumeUserName = (TextView) v.findViewById(R.id.resume_username_textview);
            resumeTime = (TextView) v.findViewById(R.id.resume_time_textview);
            deleteResume = (ImageView)v.findViewById(R.id.delete_resume);
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
                Toast.makeText(v.getContext(),"clicked "+resumeItem.getPosition() ,Toast.LENGTH_SHORT).show();
                //todo 我的简历点击后跳转到具体的简历页面 ->小榕
                Intent intent = new Intent(v.getContext(), ResumeModelDetailsActivity.class);
                intent.putExtra("resume",resumeList.get(position));//传递简历信息
                v.getContext().startActivity(intent);
            }
        });
        holder.deleteResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int position = holder.getAdapterPosition();
                ResumeItem resumeItem = mResumeList.get(position);
                Toast.makeText(v.getContext(),"clicked "+resumeItem.getPosition() ,Toast.LENGTH_SHORT).show();
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
                                        if (response.getString(PostParameterName.RESPOND_RESULTCODE).equals("200")){
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