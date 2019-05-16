package com.djylrz.xzpt.fragmentCompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.R;



public class FragmentComMine extends Fragment {

    private View mDecorView;
    private Button btnExit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDecorView = inflater.inflate(R.layout.fragment7_com_mine,container,false);
        btnExit = mDecorView.findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("token",0);
                SharedPreferences.Editor editor = sp.edit(); editor.clear(); editor.commit();
                Intent intent = new Intent(getContext(), ActorChoose.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return mDecorView;
    }


}
