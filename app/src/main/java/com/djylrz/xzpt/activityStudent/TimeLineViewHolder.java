package com.djylrz.xzpt.activityStudent;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
  *@Description: TimeLineViewHolder
  *@Author: mingjun
  *@Date: 2019/5/18 上午 1:44
  */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_timeline_date)
    TextView mDate;
    @BindView(R.id.text_timeline_title)
    TextView mMessage;
    @BindView(R.id.time_marker)
    TimelineView mTimelineView;
    @BindView(R.id.text_timeline_location)
    TextView mLocation;
    @BindView(R.id.card_timeline)
    CardView mCard;
    @BindView(R.id.linerlayout_time)
    LinearLayout linerLayoutTime;
    @BindView(R.id.linerlayout_location)
    LinearLayout linerLayoutLocation;
    Context context;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
