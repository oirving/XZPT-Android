package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.utils.RecruitmentDateAdapter;
import com.djylrz.xzpt.utils.RecruitmentDateItem;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentFindJob extends Fragment {
    private Context mContext = getContext();
    private String[] mTitles = {"推荐","热门","联系","面试技巧"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments;
    private static final String TAG = "FragmentComHome";
    private List<RecruitmentDateItem> recruitmentDateItems=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        mDecorView = inflater.inflate(R.layout.fragment6_com_home,container,false);
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }

        mTabLayout = mDecorView.findViewById(R.id.tl);
        tl();
        return mDecorView;
    }
    private void tl() {
        RecruitmentDateAdapter mRecruitmentDateAdapter= new RecruitmentDateAdapter(recruitmentDateItems);
        final ViewPager vp = mDecorView.findViewById(R.id.vp);
        vp.setAdapter(new MyPagerAdapter(this.getActivity().getSupportFragmentManager()));
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
    public void initRecruitmenDate() {
        for(int i=0;i<10;i++) {
            RecruitmentDateItem recruitmentDateItem = new RecruitmentDateItem("福州大学","待就业公司招聘会","2019-5-20 下午三四节");
            recruitmentDateItems.add(recruitmentDateItem);
        }

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

