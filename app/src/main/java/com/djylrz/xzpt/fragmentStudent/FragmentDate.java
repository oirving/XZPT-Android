package com.djylrz.xzpt.fragmentStudent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.djylrz.xzpt.R;

public class FragmentDate extends Fragment implements  View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_date,container,false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
