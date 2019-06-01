package com.djylrz.xzpt.fragment.company;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActorChoose;
import com.djylrz.xzpt.bean.Company;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xiaomi.mimc.MIMCUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class FragmentComMine extends Fragment {

    private TextView textViewCompanyName;
    private TextView textViewIntroduction;
    private TextView textViewEmail;
    private TextView textViewTel;
    private TextView textViewReview;
    private View mDecorView;
    private Button btnExit;
    private Company company = new Company();//企业实体对象
    private String token;
    private RequestQueue requestQueue;
    private static final String TAG = "FragmentComMine";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDecorView = inflater.inflate(R.layout.fragment7_com_mine, container, false);
        btnExit = mDecorView.findViewById(R.id.btn_exit);
        textViewCompanyName = mDecorView.findViewById(R.id.tv_name);
        textViewIntroduction = mDecorView.findViewById(R.id.tv_introduction);
        textViewEmail = mDecorView.findViewById(R.id.tv_email);
        textViewTel = mDecorView.findViewById(R.id.tv_telephone);
        textViewReview = mDecorView.findViewById(R.id.tv_is_review);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("token", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getContext(), ActorChoose.class);
                //注销小米消息云
                MIMCUser user = UserManager.getInstance().getUser();
                if (user != null) {
                    user.logout();
                }
                startActivity(intent);
                getActivity().finish();
            }
        });
        getCompanyInfo();
        return mDecorView;
    }

    private void getCompanyInfo() {
        //用户已经登录，查询个人信息并显示
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); //把上下文context作为参数传递进去
        SharedPreferences userToken = getActivity().getSharedPreferences("token", 0);
        token = userToken.getString(PostParameterName.TOKEN, null);
        if (token != null) {
            Log.d(TAG, "onCreate: TOKEN is " + token);
            company.setToken(token);
            try {
                Log.d(TAG, "onCreate: 获取企业信息，只填了token" + new Gson().toJson(company));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PostParameterName.POST_URL_GET_COMPANY_BY_TOKEN + company.getToken(), new JSONObject(new Gson().toJson(company)),
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "onResponse: 返回" + response.toString());
                                Type jsonType = new TypeToken<TempResponseData<Company>>() {
                                }.getType();

                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .create();
                                final TempResponseData<Company> postResult = gson.fromJson(response.toString(), jsonType);
                                Log.d(TAG, "onResponse: " + postResult.getResultCode());
                                company = postResult.getResultObject();
                                company.setToken(token);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewCompanyName.setText(company.getCompanyName());
                                        textViewIntroduction.setText(company.getDescription());
                                        textViewEmail.setText(company.getEmail());
                                        textViewTel.setText(company.getTelephone());
                                        if (company.getStatus() == 1) {
                                            textViewReview.setText("是");
                                        } else {
                                            textViewReview.setText("否");
                                        }
                                    }
                                });
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
