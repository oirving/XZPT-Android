package com.djylrz.xzpt.fragmentStudent;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;
import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.service.DownloadService;
import com.djylrz.xzpt.activityStudent.DeliveryRecordActivity;
import com.djylrz.xzpt.activityStudent.JobIntentionActivity;
import com.djylrz.xzpt.activityStudent.MyResumeActivity;
import com.djylrz.xzpt.activityStudent.PersonalInformation;
import com.djylrz.xzpt.utils.Common;
import com.djylrz.xzpt.utils.Constants;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.vondear.rxtool.view.RxToast;
import com.xiaomi.mimc.MIMCUser;

import static android.content.Context.BIND_ABOVE_CLIENT;
import static android.content.Context.NOTIFICATION_SERVICE;

public class FragmentUser extends Fragment implements View.OnClickListener{

    private LinearLayout meLayoutQuit;//退出登录
    private LinearLayout meLayoutImfornation;//个人信息
    private LinearLayout meLayoutMyResume;//我的简历
    private LinearLayout meLayoutJobIntent;//求职意向
    private LinearLayout meLayoutDeliveryRecord;//投递记录
    private LinearLayout meLayoutUserManual;//用户手册
    private LinearLayout meLayoutAdvice;//一键反馈
    private LinearLayout meLayoutUpdate;//检测更新
    private LinearLayout meLayoutAbout;//关于我们
    private View view;
    private DownloadService.DownloadBinder downloadBinder;
    private String version[];
    private String nowcode;
    private static final String TAG = "FragmentUser";
    private Handler checkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    showUpdataDialog();
                    break;
                case 2:
                    RxToast.info("已是最新版本！");
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(),DownloadService.class);
        getActivity().startService(intent);//启动服务
        getActivity().bindService(intent,connection,BIND_ABOVE_CLIENT);//绑定服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "downloadyifzu";
            String channelName = "下载";
            int importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(channelId, channelName, importance);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment5_user, container, false);
        meLayoutImfornation = view.findViewById(R.id.me_layout_information);
        meLayoutImfornation.setOnClickListener(this);
        meLayoutMyResume = view.findViewById(R.id.me_layout_my_resume);
        meLayoutMyResume.setOnClickListener(this);
        meLayoutDeliveryRecord = view.findViewById(R.id.me_layout_delivery);
        meLayoutDeliveryRecord.setOnClickListener(this);
        meLayoutJobIntent = view.findViewById(R.id.me_layout_job_intent);
        meLayoutJobIntent.setOnClickListener(this);
        meLayoutQuit = view.findViewById(R.id.me_layout_quit);
        meLayoutQuit.setOnClickListener(this);
        meLayoutUserManual = view.findViewById(R.id.me_layout_user_manual);
        meLayoutUserManual.setOnClickListener(this);
        meLayoutAdvice = view.findViewById(R.id.me_layout_advice);
        meLayoutAdvice.setOnClickListener(this);
        meLayoutAbout = view.findViewById(R.id.me_layout_about);
        meLayoutAbout.setOnClickListener(this);
        meLayoutUpdate = view.findViewById(R.id.me_layout_update);
        meLayoutUpdate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.me_layout_information:
                intent = new Intent(getContext(), PersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.me_layout_my_resume:
                intent = new Intent(getContext(), MyResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_delivery:
                intent = new Intent(getContext(), DeliveryRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_job_intent:
                intent = new Intent(getContext(), JobIntentionActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_quit:
                SharedPreferences userToken = getContext().getSharedPreferences("token",0);
                SharedPreferences.Editor editor = userToken.edit();
                editor.remove(PostParameterName.STUDENT_TOKEN);
                editor.commit();
                //注销小米消息云
                MIMCUser user = UserManager.getInstance().getUser();
                if (user != null) {
                    user.logout();
                }
                intent = new Intent(getContext(), ActorChoose.class);
                getContext().startActivity(intent);
                ((Activity)v.getContext()).finish();
                break;
            case R.id.me_layout_user_manual:
                intent = new Intent(getContext(), ActivityWebView.class);
                intent.putExtra("URL", Constants.USER_MANUAL_URL);
                startActivity(intent);
                break;
            case R.id.me_layout_advice:
                PackageManager packageManager = getActivity().getPackageManager();
                try {
                    packageManager.getPackageInfo("com.tencent.mobileqq", 0);
                } catch (PackageManager.NameNotFoundException e) {
                    RxToast.info("安装手机QQ才能与我联系哦");
                    return;
                }
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=841930898";
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                break;
            case R.id.me_layout_about:
                intent = new Intent(getContext(), ActivityWebView.class);
                intent.putExtra("URL", Constants.USER_ABOUT_ME);
                startActivity(intent);
                break;
            case R.id.me_layout_update:
                checkUpdate();
                break;
            default:
                break;
        }
    }
    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate() {
        new Thread() {
            public void run() {
                try {
                    try {
                        PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                        nowcode = packageInfo.versionName;//当前版本号
                        Log.d(TAG, "当前版本: " + nowcode);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(TAG, "启动线程检测版本更新: ");
                                    version = Common.getVersion();
                                    if (!version[0].equals(nowcode)) {
                                        Log.d(TAG, "检测到新版本！ ");
                                        checkhandler.sendEmptyMessage(1);
                                    } else {
                                        checkhandler.sendEmptyMessage(2);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setSound(null, null);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(connection);
    }
    /**
     * 弹出对话框
     */
    protected void showUpdataDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("版本升级").setMessage(version[2])
                .setCancelable(false)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadBinder.startDownload(version[1]);
                        checkhandler.sendEmptyMessage(2);
                    }
                })
                .setNegativeButton("朕知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkhandler.sendEmptyMessage(2);
                    }
                }).create().show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    RxToast.warning("拒绝权限将无法使用更新App");
                    getActivity().finish();
                }
                break;
            default:
        }
    }
}
