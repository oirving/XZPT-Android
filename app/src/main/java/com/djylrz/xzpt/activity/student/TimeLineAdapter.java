package com.djylrz.xzpt.activity.student;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;
import com.djylrz.xzpt.model.OrderStatus;
import com.djylrz.xzpt.model.Orientation;
import com.djylrz.xzpt.model.TimeLineModel;
import com.djylrz.xzpt.utils.CalendarUtils;
import com.djylrz.xzpt.utils.DateTimeUtils;
import com.djylrz.xzpt.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialogAcfunVideoLoading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.djylrz.xzpt.MyApplication.getContext;

/**
 * @Description: TODO
 * @Author: mingjun
 * @Date: 2019/5/18 上午 1:39
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
        return TimelineView.getTimeLineViewType(position, getItemCount());
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

        if (timeLineModel.getStatus() == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (timeLineModel.getStatus() == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        if (!timeLineModel.getDate().isEmpty()) {
            holder.linerLayoutTime.setVisibility(View.VISIBLE);
            holder.linerLayoutLocation.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "HH:mm a, yyyy年MM月dd日"));
        } else {
            holder.linerLayoutTime.setVisibility(View.GONE);
            holder.linerLayoutLocation.setVisibility(View.GONE);
        }
        holder.mLocation.setText(timeLineModel.getmLocation());
        holder.mMessage.setText(timeLineModel.getMessage());

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeLineModel.getmLocation() != null) {
                    Intent intent = new Intent(mContext, ActivityWebView.class);
                    intent.putExtra("URL", timeLineModel.getUrl());
                    mContext.startActivity(intent);
                }
                //Toast.makeText(mContext, "你点击了" + timeLineModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (timeLineModel.getmLocation() != null) {
                    int checkSelfPermission = 999;
                    try {
                        checkSelfPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                        //提示弹窗
                        new AlertDialog.Builder(mContext).setTitle("招聘会信息").setMessage("是否将该场招聘会添加到系统日历提醒 ")
                                .setCancelable(false)
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        Date date = new Date();
                                        try {
                                            date = df.parse(timeLineModel.getDate());
                                        } catch (ParseException pe) {
                                            pe.printStackTrace();
                                        }
                                        CalendarUtils calendarUtils = CalendarUtils.getInstance();
                                        CalendarUtils.addCalendarEvent(mContext, timeLineModel.getMessage(),
                                                "[时间] : " + timeLineModel.getDate() + "\n[地点] : " + timeLineModel.getmLocation() + "\n[网址] : " + timeLineModel.getUrl(),
                                                date.getTime(), 1);
                                        RxToast.info("已添加招聘会[" + timeLineModel.getMessage() + "]到系统日历");
                                    }
                                })
                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    } else {
                        //提示弹窗
                        new AlertDialog.Builder(mContext).setTitle("权限申请").setMessage("尚未获取访问日历权限，无法使用添加日程功能 ")
                                .setCancelable(false)
                                .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getAppDetailSettingIntent();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }

                } else {
                    new RxDialogAcfunVideoLoading(mContext).show();
                }
                //Toast.makeText(mContext, "你长按了" + timeLineModel.getMessage(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    /**
     * @Description: 跳转到系统设置页
     * @Param: []
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/30 下午 3:08
     */
    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(localIntent);
    }

}
