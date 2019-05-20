package com.djylrz.xzpt.fragmentCompany;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityCompany.AddRecruitmentActivity;
import com.djylrz.xzpt.bean.SubRecruitmentData;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentComHome extends Fragment {
    private Context mContext = getContext();
    private String[] mTitles = {"已停招岗位", "已发布岗位"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments;
    private Toolbar toolbar;//导航栏
    private static final String TAG = "FragmentComHome";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragments = new ArrayList<>();
        mDecorView = inflater.inflate(R.layout.fragment6_com_home, container, false);
        //设置标题栏
        toolbar = mDecorView.findViewById(R.id.home_toolbar);
        toolbar.setTitle("企业首页");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.add_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_hand_add:
                        //新增岗位
                        Intent intent = new Intent(getContext(), AddRecruitmentActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.home_menu_file_add:
                        Intent intent1 = new Intent(getContext(), PickCSVActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        for (String title : mTitles) {
            mFragments.add(RecruitmentCardFragment.getInstance(title));
        }

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

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.add_menu,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }


    /**
     * @Description: 打开文件选择器，选择从csv文件
     * @Param: []
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/19 上午 11:37
     */
    private void selectCSVFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);
    }

    /**
     * @Description: 读取csv文件
     * @Param: [from]
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/19 下午 12:28
     */

    //读取CSV文件
    public List<SubRecruitmentData> readCSV(String path, Activity activity) {
        List<SubRecruitmentData> list = new ArrayList<>();
        return list;
    }

}
