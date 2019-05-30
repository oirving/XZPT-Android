package com.djylrz.xzpt.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityCompany.CompanyLogin;
import com.djylrz.xzpt.activityCompany.Main2Activity;
import com.djylrz.xzpt.activityStudent.MainActivity;
import com.djylrz.xzpt.activityStudent.StudentLogin;
import com.djylrz.xzpt.bean.Company;
import com.djylrz.xzpt.bean.PostResult;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.model.ModelSVG;
import com.djylrz.xzpt.service.DownloadService;
import com.djylrz.xzpt.utils.Common;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.djylrz.xzpt.xiaomi.mimc.common.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

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
    private String userToken;
    private String companyToken;
    private User user = new User();//用户实体对象
    private Company company = new Company();//企业实体对象
    private String token;
    private RequestQueue requestQueue;
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
                                Thread.sleep(2500);
                                if (NetWorkUtils.isNetwork(mContext)) {
                                    //如果已存在token则直接登录
                                    if (userToken != null) {
                                        User user = new User();
                                        user.setToken(userToken);
                                        studentLoginWithToken(user);
                                    } else if (companyToken != null) {
                                        Company company = new Company();
                                        company.setToken(companyToken);
                                        companyLoginWithToken(company);
                                    }else{
                                        toMain();
                                    }
                                } else {
                                    toMain();
                                    RxToast.error("请检查网络连接！");
                                }
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
        //验证是否已经登录
        SharedPreferences preferences = getSharedPreferences(PostParameterName.TOKEN, 0);
        userToken = preferences.getString(PostParameterName.STUDENT_TOKEN, null);
        companyToken = preferences.getString(PostParameterName.TOKEN, null);
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


    private void studentLoginWithToken(User user) {
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue

        try {
            Log.d(TAG, "onCreate: 使用token登录" + new Gson().toJson(user));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_LOGIN_WITH_TOKEN + user.getToken(),
                    new JSONObject(new Gson().toJson(user)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到用户主界面
                                            getStudentInfo();
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            Log.d(TAG, "postLogin: 学生用户登录成功！");
                                            MyApplication.setUserType(1);
                                            startActivity(intent);
                                            finish();
                                        }
                                        break;
                                        default: {
                                            Toast.makeText(mContext, "使用token登录失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "run: 使用token登录失败，跳转用户名密码登录" + postResult.getResultCode());
                                            studentLogin();
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RxToast.error("无法连接到服务器，请检查网络连接");
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void companyLoginWithToken(final Company company) {
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue

        try {
            Log.d(TAG, "onCreate: 使用token登录" + new Gson().toJson(company));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_COMPANY_LOGIN_WITH_TOKEN + company.getToken(),
                    new JSONObject(new Gson().toJson(company)),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            final PostResult postResult = new Gson().fromJson(response.toString(), PostResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (postResult.getResultCode()) {
                                        case "200": {
                                            //跳转到企业首页
                                            Intent intent = new Intent(mContext, Main2Activity.class);
                                            startActivity(intent);
                                            Log.d(TAG, "postLogin: 企业用户登录成功！");
                                            MyApplication.setUserType(0);
                                            getCompanyInfo();
                                            finish();

                                        }
                                        break;
                                        default: {
                                            Toast.makeText(mContext, "使用token登录失败", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "run: 使用token登录失败，跳转用户名密码登录" + postResult.getResultCode());
                                            companyLogin();
                                        }

                                    }
                                }
                            });
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    RxToast.error("无法连接到服务器，请检查网络连接");
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getStudentInfo() {
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token", 0);
        token = userToken.getString(PostParameterName.STUDENT_TOKEN, null);
        if (token != null) {
            Log.d(TAG, "onCreate: TOKEN is " + token);

            user.setToken(token);

            try {
                Log.d(TAG, "onCreate: 获取个人信息，只填了token" + new Gson().toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN + user.getToken(), new JSONObject(new Gson().toJson(user)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                Type jsonType = new TypeToken<TempResponseData<User>>() {
                                }.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<User> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                user = postResult.getResultObject();
                                user.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：：" + response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("student", new Gson().toJson(user));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //设置全局userid
                                        MyApplication.userId = user.getUserId();
                                        //发消息给DemoHandler，设置mipush别名为userid
                                        Message msg = Message.obtain();
                                        msg.what = MyApplication.GET_USER_ID_SUCCESS;
                                        MyApplication.getHandler().sendMessage(msg);
                                        Log.d(TAG, "获取userid成功");
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void getCompanyInfo() {
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getSharedPreferences("token", 0);
        token = userToken.getString(PostParameterName.TOKEN, null);
        if (token != null) {
            Log.d(TAG, "onCreate: TOKEN is " + token);
            company.setToken(token);
            try {
                Log.d(TAG, "onCreate: 获取企业信息，只填了token" + new Gson().toJson(company));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_COMPANY_BY_TOKEN + company.getToken(), new JSONObject(new Gson().toJson(company)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                Type jsonType = new TypeToken<TempResponseData<Company>>() {
                                }.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<Company> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                company = postResult.getResultObject();
                                company.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("companyUser", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：：" + response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("company", new Gson().toJson(company));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //设置全局userid
                                        MyApplication.userId = company.getCompanyId();
                                        //发消息给DemoHandler，设置mipush别名为userid
                                        Message msg = Message.obtain();
                                        msg.what = MyApplication.GET_USER_ID_SUCCESS;
                                        MyApplication.getHandler().sendMessage(msg);
                                        Log.d(TAG, "获取companyId成功");
                                    }
                                });
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //学生端使用用户名密码登录
    private void studentLogin() {
        Intent student = new Intent(mContext, StudentLogin.class);
        startActivity(student);
        //finish();
        Toast.makeText(mContext, "学生用户", Toast.LENGTH_SHORT).show();
    }

    //企业端使用用户名和密码登录
    private void companyLogin() {
        Intent company = new Intent(mContext, CompanyLogin.class);
        startActivity(company);
        //finish();
        Toast.makeText(mContext, "企业用户", Toast.LENGTH_SHORT).show();
    }
}
