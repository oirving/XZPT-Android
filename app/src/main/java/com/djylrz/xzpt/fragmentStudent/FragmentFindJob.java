package com.djylrz.xzpt.fragmentStudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.bean.Recruitment;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.listener.EndlessRecyclerOnScrollListener;
import com.djylrz.xzpt.utils.LoadMoreWrapper;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.RecruitmentAdapter;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.onlynight.library.lsearchview.LSearchView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FragmentFindJob extends Fragment implements View.OnClickListener {
    private Context mContext = getContext();
    private String[] mTitles = {"推荐", "热门", "联系"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout;
    private List<Fragment> mFragments;
    private static final String TAG = "FragmentFindJob";
    private Toolbar toolbar;
    private List<Recruitment> recruitmentList = new ArrayList<Recruitment>();
    private LSearchView searchView;
    private RecruitmentAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private long limitNum = 9999;
    private SharedPreferences sharedPreferences;
    private String keyword = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        mDecorView = inflater.inflate(R.layout.fragment3_find_job, container, false);
        toolbar = mDecorView.findViewById(R.id.find_job_toolbar);
        searchView = (LSearchView) mDecorView.findViewById(R.id.searchView);
        recyclerView = (RecyclerView) mDecorView.findViewById(R.id.recycler_view_search);
        swipeRefreshLayout = mDecorView.findViewById(R.id.swipe_refresh_layout_search);
        toolbar.inflateMenu(R.menu.menu_student_recruitment_fragment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuSearch:
                        searchView.setVisibility(View.VISIBLE);
                        searchView.showWithAnim();
                        break;
                }
                return true;
            }
        });
        searchView.getSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setSearching(true);
                searchView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recruitmentList.clear();
                        keyword = searchView.getSearchEdit().getText().toString();
                        Log.d(TAG, "onClick: 查询" + keyword);
                        searchRecruitment(keyword);
                    }
                }, 1000);
            }
        });
        searchView.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackSearch();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mDecorView.getContext());
        adapter = new RecruitmentAdapter(recruitmentList, 0, getContext());
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(loadMoreWrapper);
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                recruitmentList.clear();
                currentPage = 1;
                searchRecruitment(keyword);
                loadMoreWrapper.notifyDataSetChanged();

                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                if (recruitmentList.size() < limitNum) {
                    searchRecruitment(keyword);
                } else {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });
        for (String title : mTitles) {
            if (title.equals("联系")) {
                mFragments.add(MessageCardFragment.getInstance(title));
            } else {
                mFragments.add(RecommendCardFragment.getInstance(title));
            }
        }
        mTabLayout = mDecorView.findViewById(R.id.tl);
        tl();

        sharedPreferences = getActivity().getSharedPreferences("user", 0);
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

    private void searchRecruitment(String keyword) {
        String userJson = sharedPreferences.getString("student", null);
        //todo:关于page
        PageData pageData = new PageData();
        pageData.setCurrentPage(currentPage++);
        pageData.setPageSize(PAGE_SIZE);
        if (userJson != null) {
            User user = new Gson().fromJson(userJson, User.class);
            user.getToken();
            VolleyNetUtil.getInstance().setRequestQueue(getContext().getApplicationContext());
            try {
                Log.d(TAG, "searchRecruitment: " + new Gson().toJson(pageData));
                Log.d(TAG, "searchRecruitment: URL is " + PostParameterName.POST_URL_SEARCH_RECRUIMENT + user.getToken() + "&" +
                        PostParameterName.REQUEST_KEYWORD + "=" + URLEncoder.encode(keyword, "utf-8"));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        PostParameterName.POST_URL_SEARCH_RECRUIMENT + user.getToken() + "&" +
                                PostParameterName.REQUEST_KEYWORD + "=" + URLEncoder.encode(keyword, "utf-8"),
                        new JSONObject(new Gson().toJson(pageData)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                try {
                                    switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                        case "200": {
                                            Log.d(TAG, "run: 获取招聘信息成功");
                                            //todo 解析数据
                                            JSONObject responsePageData = response.getJSONObject("resultObject");
                                            GsonBuilder builder = new GsonBuilder();
                                            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                                            builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                                public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                    return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                                }
                                            });
                                            Gson gson = builder.create();

                                            //解析pageData
                                            Type jsonType = new TypeToken<PageData<Recruitment>>() {
                                            }.getType();
                                            final PageData<Recruitment> recruitmentPageData = gson.fromJson(responsePageData.toString(), jsonType);
                                            //获取到RecruitmentList
                                            final List<Recruitment> recruitments = recruitmentPageData.getContentList();
                                            if(recruitments != null){
                                                for (Recruitment recruitment:recruitments) {
                                                    recruitmentList.add(recruitment);
                                                }
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (recruitments.size() != 0) {
                                                            RxToast.success("获取招聘信息成功");
                                                            searchView.setSearching(false);
                                                            loadMoreWrapper.notifyDataSetChanged();
                                                        } else {
                                                            RxToast.info("无符合关键词的招聘信息");
                                                        }
                                                    }
                                                });
                                            }

                                        }
                                        break;
                                        case "2018":
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    searchView.setSearching(false);
                                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                                                    limitNum = recruitmentList.size();
                                                }
                                            });
                                            break;
                                        default: {
                                            Log.d(TAG, "获取招聘信息失败" + response.getString(PostParameterName.RESPOND_RESULTCODE));
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            Log.d(TAG, "searchRecruitment: 没有UserJson");
        }
    }

    private void onBackSearch() {
        searchView.hideWithAnim();
    }

}

