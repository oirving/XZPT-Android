package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class FragmentComCheck extends Fragment {
    private View mDecorView;
    private String[] mTitles = {"已通过","已拒绝","面试中","待审核"};
    private ArrayList<Fragment> mFragments;
    private SegmentTabLayout mTabLayout;
    private Toolbar toolbar;//导航栏
    private static final String TAG = "FragmentComCheck";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDecorView =  inflater.inflate(R.layout.fragment8_com_check, container, false);
        mFragments = new ArrayList<>();
        //设置标题栏
        toolbar = mDecorView.findViewById(R.id.check_toolbar);
        toolbar.setTitle("简历审核");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        for (String title : mTitles) {
            mFragments.add(ComResumeCardFragment.getInstance(title));
        }
        mTabLayout = mDecorView.findViewById(R.id.st_layout_resume);
        tl();
        return mDecorView;
    }
    private void tl() {
        final ViewPager vp = mDecorView.findViewById(R.id.vp_resume);
        vp.setAdapter(new FragmentComCheck.MyPagerAdapter(this.getActivity().getSupportFragmentManager()));
        mTabLayout.setTabData(mTitles);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(1);
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
