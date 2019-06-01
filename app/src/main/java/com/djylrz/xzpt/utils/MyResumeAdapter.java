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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.student.EditMyResumeActivity;
import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.djylrz.xzpt.utils.PostParameterName.CHOOSE_RESUME_TO_DELIVER;

public class MyResumeAdapter extends RecyclerView.Adapter<MyResumeAdapter.ViewHolder> {

    private static final String TAG = "MyResumeAdapter";

    private List<MyResumeItem> myResumeItems;
    private OnRemoveListener onRemoveListener;

    private boolean forDeliver = false;

    public boolean isForDeliver() {
        return forDeliver;
    }

    public void setForDeliver(boolean forDeliver) {
        this.forDeliver = forDeliver;
    }

    public MyResumeAdapter(List<MyResumeItem> myResumeItems) {
        this.myResumeItems = myResumeItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View myResumeListView;
        TextView jobName;
        TextView resumeState;
        TextView companyName;
        ImageView next;
        ImageView delete;
        LinearLayout deleteButtonVisible;
        LinearLayout deleteButtonGone;
        public ViewHolder(View v) {
            super(v);
            myResumeListView = v;
            jobName = (TextView)v.findViewById(R.id.job_name);
            next = (ImageView)v.findViewById(R.id.next);
            delete = (ImageView) v.findViewById(R.id.delete);
            deleteButtonVisible = (LinearLayout)v.findViewById(R.id.delete_button_visible);
            deleteButtonVisible = (LinearLayout)v.findViewById(R.id.delete_button_gone);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myresume_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.myResumeListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MyResumeItem myResumeItem = myResumeItems.get(position);
                //跳转到EditMyResumeActivity,填入对应该简历的信息
                Intent intent = new Intent(v.getContext(), EditMyResumeActivity.class);
                intent.putExtra(Constants.INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME,Constants.EDIT_RESUME);
                //使用intent填入对应的简历信息
                intent.putExtra("editResume",myResumeItem.getResume());
                intent.putExtra("resumeID",myResumeItem.getResume().getResumeId());
                RxToast.info( Constants.EDIT_RESUME);
                if (forDeliver){
                    ((Activity)v.getContext()).setResult(CHOOSE_RESUME_TO_DELIVER,intent);//选择简历用于投递
                    ((Activity)v.getContext()).finish();//选择并投递

                }else{
                    v.getContext().startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MyResumeItem myResumeItem = myResumeItems.get(position);
        holder.jobName.setText(myResumeItem.getJobName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长按跳出删除按钮
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            //删除的事件
            public void onClick(View v) {
                if (onRemoveListener !=null) {
                    //在数据库删除对应的简历
                    //holder.delete.setVisibility(View.INVISIBLE);
                    holder.delete.setVisibility(View.GONE);
                    final int position = holder.getAdapterPosition();
                    MyResumeItem myResumeItem = myResumeItems.get(position);
                    long resumeID = myResumeItem.getResume().getResumeId();
                    String token = v.getContext().getSharedPreferences("token",0).getString(PostParameterName.STUDENT_TOKEN,null);
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
                                            //remove(position) notifyDataSetChanged()
                                            onRemoveListener.onDelete(position);
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
            }
        });

    }

    public interface OnRemoveListener {
        void onDelete(int i);
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    @Override
    public int getItemCount() {
        return myResumeItems.size();
    }

}