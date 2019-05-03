package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.djylrz.xzpt.Activity.ActorChoose;
import com.djylrz.xzpt.R;


public class FragmentComChat extends Fragment {
    private View mDecorView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDecorView = inflater.inflate(R.layout.fragment9_com_chat,container,false);

        return mDecorView;
    }


}
