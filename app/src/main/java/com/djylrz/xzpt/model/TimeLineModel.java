package com.djylrz.xzpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.djylrz.xzpt.bean.RecruitmentDate;

/**
  *@Description: TimeLineModel
  *@Author: mingjun
  *@Date: 2019/5/18 上午 1:40
  */
public class TimeLineModel implements Parcelable,Comparable<TimeLineModel>{

    private RecruitmentDate recruitmentDate;
    private String mMessage;
    private String mDate;
    private String mLocation;
    private String mHour;
    private String mMinutes;
    private OrderStatus mStatus;

    public TimeLineModel() {
    }

    public TimeLineModel(String mMessage, String mDate, OrderStatus mStatus) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = mStatus;
    }

    public TimeLineModel(RecruitmentDate recruitmentDate, OrderStatus mStatus) {
        this.recruitmentDate = recruitmentDate;
        this.mMessage = recruitmentDate.getTitle();
        //"2017-02-12 08:00";
        this.mDate = recruitmentDate.getYear()+"-"+
                recruitmentDate.getMonth()+"-"+
                recruitmentDate.getDay()+" "+
                recruitmentDate.getHour()+":"+
                recruitmentDate.getMinutes();
        this.mLocation = recruitmentDate.getLocation();
        this.mStatus = mStatus;
        this.mHour = recruitmentDate.getHour();
        this.mMinutes = recruitmentDate.getMinutes();
    }

    public String getMessage() {
        return mMessage;
    }

    public void semMessage(String message) {
        this.mMessage = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public OrderStatus getStatus() {
        return mStatus;
    }

    public void setStatus(OrderStatus mStatus) {
        this.mStatus = mStatus;
    }

    public String getmHour() {
        return mHour;
    }

    public void setmHour(String mHour) {
        this.mHour = mHour;
    }

    public String getmMinutes() {
        return mMinutes;
    }

    public void setmMinutes(String mMinutes) {
        this.mMinutes = mMinutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mDate);
        dest.writeString(this.mLocation);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
    }

    protected TimeLineModel(Parcel in) {
        this.mMessage = in.readString();
        this.mDate = in.readString();
        this.mLocation = in.readString();
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];
    }

    public static final Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };

    @Override
    public int compareTo(TimeLineModel o) {
        if(Integer.parseInt(this.getmHour())>Integer.parseInt(o.getmHour())){
            return 1;
        }else if(Integer.parseInt(this.getmHour())<Integer.parseInt(o.getmHour())){
            return -1;
        }else{
            if(Integer.parseInt(this.getmMinutes())>Integer.parseInt(o.getmMinutes())){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
