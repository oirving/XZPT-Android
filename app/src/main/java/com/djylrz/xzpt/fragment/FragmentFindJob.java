package com.djylrz.xzpt.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FragmentFindJob extends Fragment implements View.OnClickListener{
    private Context mContext = getContext();
    private String[] mTitles = {"推荐","热门","联系"};
    private View mDecorView;
    private ImageView search;
    private EditText searchEditText;
    private Button searchButton;
    private SegmentTabLayout mTabLayout;
    private List<RecommendCardFragment> mFragments;
    private static final String TAG = "FragmentFindJob";

    private SharedPreferences sharedPreferences;
    private List<RecommendCardFragment> recommendCardFragmentList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        mDecorView = inflater.inflate(R.layout.fragment3_find_job,container,false);
        for (String title : mTitles) {
            mFragments.add(RecommendCardFragment.getInstance(title));
        }

        recommendCardFragmentList=mFragments;

        search = (ImageView) mDecorView.findViewById(R.id.search_logo);
        search.setOnClickListener(this);
        searchEditText = (EditText) mDecorView.findViewById(R.id.search_edittext);
        searchEditText.setOnClickListener(this);
        searchButton = (Button) mDecorView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        mTabLayout = mDecorView.findViewById(R.id.tl);
        tl();

        sharedPreferences = getActivity().getSharedPreferences("user",0);
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
                Log.d(TAG, "onClick: 查询按钮。");
                Log.d(TAG, "onClick:查询按钮可见"+searchButton.getVisibility());
                Log.d(TAG, "onClick:查询文本可见"+searchButton.getVisibility());
                if(searchEditText.getVisibility()==View.VISIBLE&&searchButton.getVisibility()==View.VISIBLE) {
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
                String keyword = searchEditText.getText().toString();
                Log.d(TAG, "onClick: 查询");
                searchRecruitment(keyword);
                break;
            default:
                Log.d(TAG, "onClick: default");
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

    private void searchRecruitment(String keyword){
        String userJson = sharedPreferences.getString("student",null);
        //todo:关于page
        PageData pageData = new PageData();
        pageData.setCurrentPage(1);
        pageData.setPageSize(10);
        if (userJson!=null){
            User user = new Gson().fromJson(userJson,User.class);
            user.getToken();
            VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());
            try {
                Log.d(TAG, "searchRecruitment: "+new Gson().toJson(pageData));
                Log.d(TAG, "searchRecruitment: URL is "+ PostParameterName.POST_URL_SEARCH_RECRUIMENT+user.getToken()+"&"+
                        PostParameterName.REQUEST_KEYWORD+"="+keyword);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        PostParameterName.POST_URL_SEARCH_RECRUIMENT+user.getToken()+"&"+
                        PostParameterName.REQUEST_KEYWORD+"="+keyword,
                        new JSONObject(new Gson().toJson(pageData)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, "onResponse: 返回"+response.toString());
                                List<Recruitment> recruitments = null;
                                try {
                                    switch (response.getString(PostParameterName.RESPOND_RESULTCODE)){
                                        case "200":{
                                            Log.d(TAG, "run: 获取招聘信息成功");
                                            //todo 解析数据
                                            JSONObject responsePageData = response.getJSONObject("resultObject");

                                            GsonBuilder builder = new GsonBuilder();
                                            builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                    return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                                }
                                            });
                                            Gson gson = builder.create();


                                            //解析pageData
                                            Type jsonType = new TypeToken<PageData<Recruitment>>() {}.getType();
                                            final PageData<Recruitment> recruitmentPageData = gson.fromJson(responsePageData.toString(),jsonType);

                                            //获取到RecruitmentList
                                            recruitments = recruitmentPageData.getContentList();
                                            Log.d(TAG, "onResponse: "+recruitments.size());;
                                            //todo:获取到数据之后的处理
                                        }break;
                                        default:{
                                            Log.d(TAG, "获取招聘信息失败"+response.getString(PostParameterName.RESPOND_RESULTCODE));
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                final List<Recruitment> finalRecruitments = recruitments;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "获取招聘信息成功", Toast.LENGTH_SHORT).show();
                                        //todo 更新页面
                                        finalRecruitments.size();
                                        for(RecommendCardFragment recommendCardFragment:recommendCardFragmentList){
                                            recommendCardFragment.updateAdapter(finalRecruitments);
                                        }
                                    }
                                });
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }});
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Log.d(TAG, "searchRecruitment: 没有UserJson");
        }
    }
}

