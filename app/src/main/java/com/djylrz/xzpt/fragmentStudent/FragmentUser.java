package com.djylrz.xzpt.fragmentStudent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;
import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.activityStudent.DeliveryRecordActivity;
import com.djylrz.xzpt.activityStudent.JobIntentionActivity;
import com.djylrz.xzpt.activityStudent.MyResumeActivity;
import com.djylrz.xzpt.activityStudent.PersonalInformation;
import com.djylrz.xzpt.utils.Constants;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.xiaomi.mimc.MIMCUser;

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
                    Toast.makeText(getActivity(), "安装手机QQ才能与我联系哦", Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }


}
