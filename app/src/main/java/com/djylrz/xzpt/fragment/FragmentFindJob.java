package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.djylrz.xzpt.R;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class FragmentFindJob extends Fragment implements View.OnClickListener{
    private Context mContext = getContext();
    private String[] mTitles = {"推荐","热门","联系"};
    private View mDecorView;
    private ImageView search;
    private EditText searchEditText;
    private Button searchButton;
    private SegmentTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments;
    private static final String TAG = "FragmentFindJob";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        mDecorView = inflater.inflate(R.layout.fragment3_find_job,container,false);
        for (String title : mTitles) {
            mFragments.add(RecommendCardFragment.getInstance(title));
        }
        search = (ImageView) mDecorView.findViewById(R.id.search_logo);
        search.setOnClickListener(this);
        searchEditText = (EditText) mDecorView.findViewById(R.id.search_edittext);
        searchButton = (Button) mDecorView.findViewById(R.id.search_button);
        mTabLayout = mDecorView.findViewById(R.id.tl);
        tl();
        return mDecorView;
    }
    private void tl() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_logo:
                if(searchEditText.getVisibility()==View.VISIBLE||searchButton.getVisibility()==View.VISIBLE) {
                    searchButton.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                } else {
                    searchButton.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.VISIBLE);
                    mTabLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.search_button:
                //todo 返回查询结果 ->小榕
                break;
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

