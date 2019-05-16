package com.djylrz.xzpt;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.djylrz.xzpt.activityStudent.MainActivity;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个 Application。
 * 并将自定义的 application 注册在 AndroidManifest.xml 文件中。<br/>
 * 2、为了提高 push 的注册率，您可以在 Application 的 onCreate 中初始化 push。你也可以根据需要，在其他地方初始化 push。
 *
 * @author wangkuiwei
 */
public class MyApplication extends Application {

    // user your appid the key.
    private static final String APP_ID = "2882303761518007113";
    // user your appid the key.
    private static final String APP_KEY = "5301800721113";
    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    public static final String TAG = "com.djylrz.xzpt";
    //全局常量
    public static final int GET_USER_ID_SUCCESS = 1;
    public static final int REGISTER_XMPUSH_SUCCESS = 2;

    private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;

    //全局用户id
    public static String userId = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册push服务，注册成功后会向BroadcastReceiver发送广播
        // 可以从BroadcastReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void reInitPush(Context ctx) {
        MiPushClient.registerPush(ctx.getApplicationContext(), APP_ID, APP_KEY);
    }

    public static DemoHandler getHandler() {
        return sHandler;
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    public static class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyApplication.GET_USER_ID_SUCCESS:
                    //设置XMPUSH别名
                    MiPushClient.setAlias(context, userId, null);
                    //显示设置XMPUSH别名提示
                    Toast.makeText(context, "设置别名成功："+userId, Toast.LENGTH_LONG).show();
                    break;
                case MyApplication.REGISTER_XMPUSH_SUCCESS:
                    //显示xmpush注册成功提示
                    String s = (String) msg.obj;
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    }
}