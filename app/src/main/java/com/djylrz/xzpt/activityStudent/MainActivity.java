package com.djylrz.xzpt.activityStudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.BaseActivity;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.fragment.FragmentAdapter;
import com.djylrz.xzpt.fragmentStudent.FragmentDate;
import com.djylrz.xzpt.fragmentStudent.FragmentFindJob;
import com.djylrz.xzpt.fragmentStudent.FragmentResume;
import com.djylrz.xzpt.fragmentStudent.FragmentTips;
import com.djylrz.xzpt.fragmentStudent.FragmentUser;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :oirving
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = "UserOption";

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private BottomNavigationView navigation;

    private String token;
    private User user = new User();
    private FragmentUser fragmentUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_date:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_resume:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_find_job:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_experience:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_user:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    /**
     * 初始化fragment
     */
    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //向ViewPager添加各页面
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentDate());
        fragmentList.add(new FragmentResume());
        fragmentList.add(new FragmentFindJob());
        fragmentList.add(new FragmentTips());

        fragmentUser = new FragmentUser();
        fragmentList.add(fragmentUser);
        getStudenInfo();

        FragmentAdapter myAdapter = new FragmentAdapter(getSupportFragmentManager(), this, fragmentList);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //注意这个方法滑动时会调用多次，下面是参数解释：
                //position当前所处页面索引,滑动调用的最后一次绝对是滑动停止所在页面
                //positionOffset:表示从位置的页面偏移的[0,1]的值。
                //positionOffsetPixels:以像素为单位的值，表示与位置的偏移
            }

            @Override
            public void onPageSelected(int position) {
                //该方法只在滑动停止时调用，position滑动停止所在页面位置
//                当滑动到某一位置，导航栏对应位置被按下
                navigation.getMenu().getItem(position).setChecked(true);
                //这里使用navigation.setSelectedItemId(position);无效，
                //setSelectedItemId(position)的官网原句：Set the selected
                // menu item ID. This behaves the same as tapping on an item
                //未找到原因
            }


            @Override
            public void onPageScrollStateChanged(int state) {
//这个方法在滑动是调用三次，分别对应下面三种状态
// 这个方法对于发现用户何时开始拖动，
// 何时寻呼机自动调整到当前页面，或何时完全停止/空闲非常有用。
//                state表示新的滑动状态，有三个值：
//                SCROLL_STATE_IDLE：开始滑动（空闲状态->滑动），实际值为0
//                SCROLL_STATE_DRAGGING：正在被拖动，实际值为1
//                SCROLL_STATE_SETTLING：拖动结束,实际值为2
            }
        });
    }

    private void getStudenInfo() {
        //用户已经登录，查询个人信息并显示
        VolleyNetUtil.getInstance().setRequestQueue(getApplicationContext());//获取requestQueue
        SharedPreferences userToken = getSharedPreferences("token", 0);
        token = userToken.getString(PostParameterName.STUDENT_TOKEN, null);
        if (token != null) {
            Log.d(TAG, "onCreate: TOKEN is " + token);
            user.setToken(token);

            try {
                Log.d(TAG, "onCreate: 获取个人信息，只填了token" + new Gson().toJson(user));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_USER_BY_TOKEN + user.getToken(), new JSONObject(new Gson().toJson(user)),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                Type jsonType = new TypeToken<TempResponseData<User>>() {
                                }.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<User> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                user = postResult.getResultObject();
                                user.setToken(token);

                                //获取用户信息，存储到本地。
                                SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    Log.d(TAG, "用户信息存储到本地SharedPreferences：：" + response.getJSONObject(PostParameterName.RESPOND_RESULTOBJECT).toString());
                                    editor.putString("student", new Gson().toJson(user));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.commit();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        fragmentUser.setUserName(user);
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
