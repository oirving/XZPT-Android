package com.djylrz.xzpt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.RecyclerDivider;
import com.djylrz.xzpt.utils.UserInfoOptionAdapter;
import com.djylrz.xzpt.utils.UserSelector;

import java.util.ArrayList;
import java.util.List;

public class FragmentUser extends Fragment {

    private RecyclerView userOption;
    private UserInfoOptionAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private UserSelector myResume,jobInention,focusCompany,help;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //初始化我的页面选项
        List<UserSelector> userSelectorList = new ArrayList<>();
        myResume= new UserSelector(R.drawable.resume,"我 的 简 历 >");
        userSelectorList.add(myResume);

        jobInention = new UserSelector(R.drawable.card,"求 职 意 向 管 理 >");
        userSelectorList.add(jobInention);

        focusCompany = new UserSelector(R.drawable.giftcard,"关 注 公 司 >");
        userSelectorList.add(focusCompany);

        help = new UserSelector(R.drawable.help,"帮 助 与 反 馈 >");
        userSelectorList.add(help);

        View view = inflater.inflate(R.layout.fragment5_user, container, false);
        userOption = (RecyclerView) view.findViewById(R.id.user_option_list);
        linearLayoutManager =new LinearLayoutManager(getContext());
        adapter = new UserInfoOptionAdapter(userSelectorList);
        userOption.setAdapter(adapter);
        userOption.setLayoutManager(linearLayoutManager);
        userOption.addItemDecoration (new RecyclerDivider(getContext(), LinearLayoutManager.HORIZONTAL, 3,  R.color.colorDark));
        return view;
    }
}
