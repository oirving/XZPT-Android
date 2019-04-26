package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.UserSelector;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    Context context;
    List<Fragment> listFragment;

    public FragmentAdapter(FragmentManager fm, Context context, List<Fragment> listFragment) {
        super(fm);
        this.context = context;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
