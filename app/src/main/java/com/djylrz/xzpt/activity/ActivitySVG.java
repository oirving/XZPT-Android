package com.djylrz.xzpt.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.model.ModelSVG;
import com.djylrz.xzpt.service.DownloadService;
import com.djylrz.xzpt.utils.Common;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxui.activity.ActivityBase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author vondear
 */
public class ActivitySVG extends ActivityBase {

    @BindView(R.id.animated_svg_view)
    AnimatedSvgView mSvgView;
    @BindView(R.id.activity_svg)
    RelativeLayout mActivitySvg;
    @BindView(R.id.app_name)
    ImageView mAppName;

    private DownloadService.DownloadBinder downloadBinder;
    private String version[];
    private String nowcode;
    private static final String TAG = "ActivitySVG";
    private Handler checkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (ContextCompat.checkSelfPermission(ActivitySVG.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ActivitySVG.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    showUpdataDialog();
                    break;
                case 2:
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(500);
                                Thread.sleep(2000);
                                toMain();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);//启动服务
        bindService(intent,connection,BIND_ABOVE_CLIENT);//绑定服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "downloadyifzu";
            String channelName = "下载";
            int importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(channelId, channelName, importance);
        }
        RxBarTool.hideStatusBar(this);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        setSvg(ModelSVG.values()[12]);
        checkUpdate();
    }

    private void setSvg(ModelSVG modelSvg) {
        mSvgView.setGlyphStrings(modelSvg.glyphs);
        mSvgView.setFillColors(modelSvg.colors);
        mSvgView.setViewportSize(modelSvg.width, modelSvg.height);
        mSvgView.setTraceResidueColor(0x32000000);
        mSvgView.setTraceColors(modelSvg.colors);
        mSvgView.rebuildGlyphData();
        mSvgView.start();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate() {
        new Thread() {
            public void run() {
                try {
                    try {
                        PackageInfo packageInfo = getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0);
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

    public void toMain() {
        RxActivityTool.skipActivityAndFinish(this, ActorChoose.class);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setSound(null, null);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
    /**
     * 弹出对话框
     */
    protected void showUpdataDialog() {
        new android.app.AlertDialog.Builder(this)
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
                    Toast.makeText(this, "拒绝权限将无法使用更新App", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

}
