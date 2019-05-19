package com.djylrz.xzpt.fragmentStudent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.activityStudent.BasicResumeActivity;
import com.djylrz.xzpt.activityStudent.FocusCompanyActivity;
import com.djylrz.xzpt.activityStudent.JobIntention;
import com.djylrz.xzpt.activityStudent.MyResumeActivity;
import com.djylrz.xzpt.activityStudent.PersonalInformation;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.UserInfoOptionAdapter;
import com.djylrz.xzpt.utils.UserSelector;

public class FragmentUser extends Fragment implements View.OnClickListener{

    private RecyclerView userOption;
    private UserInfoOptionAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private UserSelector myResume,jobInention,focusCompany,help,logout;
    private LinearLayout meLayoutQuit;//退出登录
    private LinearLayout meLayoutImfornation;//个人信息
    private LinearLayout meLayoutMyResume;//我的简历
    private LinearLayout meLayoutJobIntent;//求职意向
    private LinearLayout meLayoutDeliveryRecord;//投递记录
    private LinearLayout meLayoutFocusCompany;//关注公司

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
        meLayoutFocusCompany = view.findViewById(R.id.me_layout_focus_company);
        meLayoutFocusCompany.setOnClickListener(this);
        meLayoutQuit = view.findViewById(R.id.me_layout_quit);
        meLayoutQuit.setOnClickListener(this);

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
                intent = new Intent(getContext(), BasicResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_delivery:
                intent = new Intent(getContext(),MyResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_job_intent:
                intent = new Intent(getContext(), JobIntention.class);
                startActivity(intent);
                break;
            case R.id.me_layout_focus_company:
                intent = new Intent(getContext(), FocusCompanyActivity.class);
                startActivity(intent);
                break;
            case R.id.me_layout_quit:
                SharedPreferences userToken = getContext().getSharedPreferences("token",0);
                SharedPreferences.Editor editor = userToken.edit();
                editor.remove(PostParameterName.STUDENT_TOKEN);
                editor.commit();
                intent = new Intent(getContext(), ActorChoose.class);
                getContext().startActivity(intent);
                ((Activity)v.getContext()).finish();
                break;
            default:
                break;
        }
    }


}
