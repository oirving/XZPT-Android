package com.djylrz.xzpt.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.activityStudent.RecruitmentDetailActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * @Description: 接收mipush消息
 * @Author mingjun
 * @Title: BroadcastReceiver
 * @ProjectName XZPT-Android
 * @Date 2019/5/16下午 8:19
 */
public class BroadcastReceiver extends PushMessageReceiver {
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
//        if(!TextUtils.isEmpty(message.getTopic())) {
//            mTopic=message.getTopic();
//        } else if(!TextUtils.isEmpty(message.getAlias())) {
//            mAlias=message.getAlias();
//        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
//            mUserAccount=message.getUserAccount();
//        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
//        if(!TextUtils.isEmpty(message.getTopic())) {
//            mTopic=message.getTopic();
//        } else if(!TextUtils.isEmpty(message.getAlias())) {
//            mAlias=message.getAlias();
//        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
//            mUserAccount=message.getUserAccount();
//        }
        //跳转到岗位详情
        Intent intent = new Intent(MyApplication.getContext(), RecruitmentDetailActivity.class);
        intent.putExtra("recruitmentID",Long.parseLong(mMessage));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (mMessage != null){
//            Intent intent = new Intent(MyApplication.getContext(), ActorChoose.class);
//            intent.putExtra("recruitmentID", Long.parseLong(mMessage));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              MyApplication.getContext().startActivity(intent);
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
//        if(!TextUtils.isEmpty(message.getTopic())) {
//            mTopic=message.getTopic();
//        } else if(!TextUtils.isEmpty(message.getAlias())) {
//            mAlias=message.getAlias();
//        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
//            mUserAccount=message.getUserAccount();
//        }
    }

    //onCommandResult用来接收客户端向服务器发送命令消息后返回的响应
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }

    //onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.v(MyApplication.TAG,
                "onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            //XMPUSH注册成功，获取唯一的regId.
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.register_success);
                mRegId = cmdArg1;
                //Log.d(MyApplication.TAG, "注册id为"+mRegId);
                //设置别名为用户id
                //MiPushClient.setAlias(this, 用户唯一id, null);
                //设置userAccount
                //MiPushClient.setUserAccount(this, account, null);
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else {
            log = message.getReason();
        }
        Message msg = Message.obtain();
        msg.what = MyApplication.REGISTER_XMPUSH_SUCCESS;
        msg.obj = log;
        MyApplication.getHandler().sendMessage(msg);
    }
}
