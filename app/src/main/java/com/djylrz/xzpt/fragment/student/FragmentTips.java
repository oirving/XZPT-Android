package com.djylrz.xzpt.fragment.student;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.PageData;
import com.djylrz.xzpt.utils.ImageCarousel;
import com.djylrz.xzpt.utils.ImageInfo;
import com.djylrz.xzpt.utils.InterviewSkill;
import com.djylrz.xzpt.utils.InterviewTipsAdapter;
import com.djylrz.xzpt.utils.InterviewTipsItem;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.utils.VolleyNetUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.vondear.rxtool.view.RxToast;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.smtt.sdk.TbsReaderView.TAG;

public class FragmentTips extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点
    private List<InterviewTipsItem> tips = new ArrayList<>();//每条技巧
    private InterviewTipsAdapter interviewTipsAdapter;
    public LRecyclerView lRecyclerView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    static SecureRandom rnd = new SecureRandom();
    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;
    private int currentPage = 1;
    static final ArrayList<String> MESSAGES = new ArrayList<String>() {
        {
            add("不要后悔做任何事情，因为曾经有个时候，那正是你想要的");
            add("Don't regret anything, because there was a time, that's exactly what you want");
            add("孤单，是你心里面没有人。 寂寞，是你心里有人却不在身边");
            add("No one is alone, is your heart. Lonely, is your heart someone but not around");
            add("非要经历一些无常，才能甘愿珍惜当下");
            add("Have to experience some uncertainty to willing to cherish the present");
            add("也许我们都没变，只是越来越接近真实的自己");
            add("Perhaps we didn't change, just more and more close to the real you");
            add("时光，就这样在我们回首追寻中，兜兜转转间，一去不返");
            add("人生是一场负重的奔跑，需要不停地在每一个岔路口做出选择");
            add("Never trouble trouble till trouble troubles you");
            add("但凡不能杀死你的，最终都会使你更强大");
            add("That which does not kill us makes us stronger");
            add("我的生活不曾取悦于我，所以我创造了自己的生活");
            add("别让你的骄傲使你孤独一人");
            add("如果你有足够的勇气，那么一切皆有可能");
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    //interviewTipsAdapter.addAll(tips);
                    lRecyclerView.refreshComplete(10);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    RxToast.error("加载出错[好像没网了哦]");
                    lRecyclerView.refreshComplete(10);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    lRecyclerView.setNoMore(true);
                    break;
                case 7:
                    //interviewTipsAdapter.addAll(tips);
                    lRecyclerView.refreshComplete(10);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 8:
                    RxToast.error("加载出错[好像没网了哦]");
                    lRecyclerView.refreshComplete(10);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4_tips, container, false);
        initView(view);
        initEvent();
        imageStart(view);
        //initTips();
        return view;
    }

    private void initEvent() {
        imageInfoList = new ArrayList<>();
        //todo 填入title、image、url就可以了，image是图片的url,url是点击后跳转的页面的URL ->小榕
        imageInfoList.add(new ImageInfo(1, "校园波斯菊盛开 蝶舞蜂飞忙不停（福友阁旁）", "", "https://www.fzu.edu.cn/attach/2019/05/27/347300.jpg", "https://www.fzu.edu.cn/"));
        imageInfoList.add(new ImageInfo(1, "夏日·蓝花楹（学生活动中心旁）", "", "https://www.fzu.edu.cn/attach/2019/05/22/346750.jpg", "https://www.fzu.edu.cn/"));
        imageInfoList.add(new ImageInfo(1, "光影福大", "", "https://www.fzu.edu.cn/attach/2019/05/13/345520.jpg", "https://www.fzu.edu.cn/"));
        imageInfoList.add(new ImageInfo(1, "福大樱花季（福友阁）", "", "https://www.fzu.edu.cn/attach/2019/04/24/343183.jpg", "https://www.fzu.edu.cn/"));
        imageInfoList.add(new ImageInfo(1, "校园风光（图书馆）", "", "https://www.fzu.edu.cn/attach/2019/04/15/341999.jpg", "https://www.fzu.edu.cn/"));
    }

//    private void initTips() {
//        /*for (int i=0;i<7;i++) {
//            //todo 按照这个格式把有的数据填入,title,url：点击后跳转的url ->
//            tips.add(new InterviewTipsItem("如何冷静面对面试官","https://baijiahao.baidu.com/s?id=1608574900105467287&wfr=spider&for=pc"));
//        }*/
//        PageData pageData = new PageData();
//        pageData.setCurrentPage(1);
//        pageData.setPageSize(20);
//
//        try {
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    PostParameterName.POST_URL_INTERVIEW_SKILL_PAGE,
//                    new JSONObject(new Gson().toJson(pageData)),
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d(TAG, "onResponse: 返回" + response.toString());
//                            try {
//                                switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
//                                    case "200": {
//                                        JSONObject pageDataResultObject = response.getJSONObject("resultObject");
//
//                                        GsonBuilder builder = new GsonBuilder();
//                                        builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
//                                            public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
//                                                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
//                                            }
//                                        });
//                                        Gson gson = builder
//                                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//
//                                        //解析pageData
//                                        Type jsonType = new TypeToken<PageData<InterviewSkill>>() {
//                                        }.getType();
//                                        final PageData<InterviewSkill> resumePageData = gson.fromJson(pageDataResultObject.toString(), jsonType);
//
//                                        //获取到ResumeList
//                                        List<InterviewSkill> interviewSkills = resumePageData.getContentList();
//                                        tips.clear();
//                                        for (InterviewSkill interviewSkill : interviewSkills) {
//                                            //简历表无创建简历时间，无岗位意向（求职意向中有）
//                                            InterviewTipsItem interviewTipsItem = new InterviewTipsItem(
//                                                    interviewSkill);
//                                            tips.add(interviewTipsItem);
//                                            interviewTipsAdapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                    break;
//                                    default:
//                                        handler.sendEmptyMessage(6);
//                                    break;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("TAG Response failed", error.getMessage(), error);
//                }
//            });
//
//            VolleyNetUtil.getInstance().setRequestQueue(getContext()
//            );//获取requestQueue
//            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
        mTvPagerTitle = view.findViewById(R.id.tv_pager_title);
        mLineLayoutDot = view.findViewById(R.id.lineLayout_dot);
//        tipsList = (RecyclerView) view.findViewById(R.id.interviewer_tips);
        lRecyclerView = view.findViewById(R.id.interviewer_tips);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        interviewTipsAdapter = new InterviewTipsAdapter(tips);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(interviewTipsAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setHeaderViewColor(R.color.colorPrimary, R.color.gallery_dark_gray, android.R.color.white);
        lRecyclerView.setFooterViewColor(R.color.colorPrimary, R.color.gallery_dark_gray, android.R.color.white);
        lRecyclerView.setFooterViewHint("拼命加载中", "只有这么多啦", "网络不给力啊，点击再试一次吧");
        lRecyclerView.setLoadMoreEnabled(true);
        lRecyclerView.setHasFixedSize(true);
        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);
        lRecyclerView.refresh();
    }

    private void imageStart(View view) {
        //设置图片轮播
        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,
                R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
        String[] titles = new String[imageInfoList.size()];
        String[] urls = new String[imageInfoList.size()];
        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

        for (int i = 0; i < imageInfoList.size(); i++) {
            titles[i] = imageInfoList.get(i).getTitle();
            urls[i] = imageInfoList.get(i).getUrl();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getContext());
            simpleDraweeView.setAspectRatio(1.78f);
            // 设置一张默认的图片
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(this.getResources())
                    .setPlaceholderImage(ContextCompat.getDrawable(getContext(), R.drawable.logo_fzu_h), ScalingUtils.ScaleType.CENTER_CROP).build();
            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            //加载高分辨率图片;
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                    .setResizeOptions(new ResizeOptions(1280, 720))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);

            simpleDraweeView.setId(imgaeIds[i]);//给view设置id
            simpleDraweeView.setTag(imageInfoList.get(i));
            final String url = urls[i];
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    //跳转到一个webview
//                    Intent intent = new Intent(v.getContext(), ActivityWebView.class);
//                    intent.putExtra("URL", url);
//                    v.getContext().startActivity(intent);
                    //吐司一个鸡汤
                    RxToast.success(getRandomChickenSoup());
                }
            });
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(mLineLayoutDot, fromResToDrawable(getContext(), R.drawable.ic_dot_focused), simpleDraweeViewList.size(), view);
        imageCarousel = new ImageCarousel(getContext(), mViewPager, mTvPagerTitle, dots, 5000);
        imageCarousel.init(simpleDraweeViewList, titles)
                .startAutoPlay();
        imageCarousel.start();
    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return 小点的Id
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.addView(dot);
            }
        });

        return dot.getId();
    }


    /**
     * 资源图片转Drawable
     *
     * @param context 上下文
     * @param resId   资源ID
     * @return 返回Drawable图像
     */
    public static Drawable fromResToDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
        //return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout 线性横向布局
     * @param backgount    小点资源图标
     * @param number       数量
     * @return 返回小点View集合
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number, View view) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }

    @Override
    public void onLoadMore() {
        PageData pageData = new PageData();
        pageData.setCurrentPage(++currentPage);
        pageData.setPageSize(10);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    PostParameterName.POST_URL_INTERVIEW_SKILL_PAGE,
                    new JSONObject(new Gson().toJson(pageData)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            try {
                                switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                    case "200": {
                                        JSONObject pageDataResultObject = response.getJSONObject("resultObject");

                                        GsonBuilder builder = new GsonBuilder();
                                        builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                            @Override
                                            public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                            }
                                        });
                                        Gson gson = builder
                                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                                        //解析pageData
                                        Type jsonType = new TypeToken<PageData<InterviewSkill>>() {
                                        }.getType();
                                        final PageData<InterviewSkill> resumePageData = gson.fromJson(pageDataResultObject.toString(), jsonType);

                                        //获取到ResumeList
                                        List<InterviewSkill> interviewSkills = resumePageData.getContentList();
//                                        tips.clear();
                                        for (InterviewSkill interviewSkill : interviewSkills) {
                                            //简历表无创建简历时间，无岗位意向（求职意向中有）
                                            InterviewTipsItem interviewTipsItem = new InterviewTipsItem(
                                                    interviewSkill);
                                            tips.add(interviewTipsItem);
                                            handler.sendEmptyMessage(7);
                                        }
                                    }
                                    break;
                                    default:
                                        handler.sendEmptyMessage(6);
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG Response failed", error.getMessage(), error);
                }
            });

            VolleyNetUtil.getInstance().setRequestQueue(getContext()
            );//获取requestQueue
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        interviewTipsAdapter.clear();
        currentPage = 1;
        PageData pageData = new PageData();
        pageData.setCurrentPage(1);
        pageData.setPageSize(10);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    PostParameterName.POST_URL_INTERVIEW_SKILL_PAGE,
                    new JSONObject(new Gson().toJson(pageData)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: 返回" + response.toString());
                            try {
                                switch (response.getString(PostParameterName.RESPOND_RESULTCODE)) {
                                    case "200": {
                                        JSONObject pageDataResultObject = response.getJSONObject("resultObject");

                                        GsonBuilder builder = new GsonBuilder();
                                        builder.registerTypeAdapter(Timestamp.class, new com.google.gson.JsonDeserializer<Timestamp>() {
                                            @Override
                                            public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
                                            }
                                        });
                                        Gson gson = builder
                                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                                        //解析pageData
                                        Type jsonType = new TypeToken<PageData<InterviewSkill>>() {
                                        }.getType();
                                        final PageData<InterviewSkill> resumePageData = gson.fromJson(pageDataResultObject.toString(), jsonType);

                                        //获取到ResumeList
                                        List<InterviewSkill> interviewSkills = resumePageData.getContentList();
                                        tips.clear();
                                        for (InterviewSkill interviewSkill : interviewSkills) {
                                            //简历表无创建简历时间，无岗位意向（求职意向中有）
                                            InterviewTipsItem interviewTipsItem = new InterviewTipsItem(
                                                    interviewSkill);
                                            tips.add(interviewTipsItem);
                                            handler.sendEmptyMessage(1);
                                        }
                                    }
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG Response failed", error.getMessage(), error);
                }
            });

            VolleyNetUtil.getInstance().setRequestQueue(getContext()
            );//获取requestQueue
            VolleyNetUtil.getInstance().getRequestQueue().add(jsonObjectRequest);//添加request
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getRandomChickenSoup(){
        return MESSAGES.get(rnd.nextInt(MESSAGES.size()));
    }
}
