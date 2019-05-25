package com.djylrz.xzpt.activityStudent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;
import com.djylrz.xzpt.model.OrderStatus;
import com.djylrz.xzpt.model.Orientation;
import com.djylrz.xzpt.model.TimeLineModel;
import com.djylrz.xzpt.utils.DateTimeUtils;
import com.djylrz.xzpt.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.vondear.rxui.view.dialog.RxDialogSure;

import java.util.List;

/**
  *@Description: TODO
  *@Author: mingjun
  *@Date: 2019/5/18 上午 1:39
  */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<TimeLineModel> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

            view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false);

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, int position) {

        final TimeLineModel timeLineModel = mFeedList.get(position);

        if(timeLineModel.getStatus() == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        if(!timeLineModel.getDate().isEmpty()) {
            holder.linerLayoutTime.setVisibility(View.VISIBLE);
            holder.linerLayoutLocation.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "HH:mm a, yyyy年MM月dd日"));
        }
        else {
            holder.linerLayoutTime.setVisibility(View.GONE);
            holder.linerLayoutLocation.setVisibility(View.GONE);
        }
        holder.mLocation.setText(timeLineModel.getmLocation());
        holder.mMessage.setText(timeLineModel.getMessage());

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeLineModel.getmLocation() != null){
                    Intent intent = new Intent(mContext, ActivityWebView.class);
                    intent.putExtra("URL",timeLineModel.getURL());
                    mContext.startActivity(intent);
                }
                //Toast.makeText(mContext, "你点击了" + timeLineModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //提示弹窗
                new AlertDialog.Builder(mContext).setTitle("招聘会信息").setMessage("[标题] : " + timeLineModel.getMessage()
                        + "\n[地点] : " + timeLineModel.getmLocation()
                        + "\n[日期] : " + timeLineModel.getDate()
                        + "\n[网址] : " + timeLineModel.getURL())
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                //Toast.makeText(mContext, "你长按了" + timeLineModel.getMessage(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}
