package com.djylrz.xzpt.fragmentStudent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;
import com.djylrz.xzpt.utils.ImageCarousel;
import com.djylrz.xzpt.utils.ImageInfo;
import com.djylrz.xzpt.utils.InterviewTipsAdapter;
import com.djylrz.xzpt.utils.InterviewTipsItem;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class FragmentTips extends Fragment {

    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点
    private List<InterviewTipsItem> tips = new ArrayList<>();//每条技巧
    private RecyclerView tipsList;
    private InterviewTipsAdapter interviewTipsAdapter;
    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4_tips,container,false);
        initView(view);
        initEvent();
        imageStart(view);
        initTips();
        return view;
    }

    private void initEvent() {
        imageInfoList = new ArrayList<>();
        //todo 填入title、image、url就可以了，image是图片的url,url是点击后跳转的页面的URL ->小榕
        imageInfoList.add(new ImageInfo(1, "快乐减压 轻松迎考", "", "http://www.xinhuanet.com/photo/2019-05/25/1124541200_15587688147361n.jpg", "http://baidu.com/"));
        imageInfoList.add(new ImageInfo(1, "捷克开始欧洲议会选举投票", "", "http://www.xinhuanet.com/photo/2019-05/25/1124541094_15587662540611n.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片3，待就业六人组", "", "http://e.hiphotos.baidu.com/image/pic/item/6a600c338744ebf85ed0ab2bd4f9d72a6059a705.jpg", "https://org.modao.cc/app/950598c672c518ab5bd04e88bfa7fa1d#screen=s94715B7C8A1556100983973"));
        imageInfoList.add(new ImageInfo(1, "图片4，待就业六人组", "仅展示", "http://b.hiphotos.baidu.com/image/h%3D300/sign=8ad802f3801001e9513c120f880e7b06/a71ea8d3fd1f4134be1e4e64281f95cad1c85efa.jpg", "http://www.xinhuanet.com/photo/2019-05/25/c_1124541200_2.htm"));
        imageInfoList.add(new ImageInfo(1, "图片5，待就业六人组", "仅展示", "http://e.hiphotos.baidu.com/image/h%3D300/sign=73443062281f95cab9f594b6f9177fc5/72f082025aafa40fafb5fbc1a664034f78f019be.jpg", "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=84053098_3_dg&wd=java.lang.NullPointerException%3A%20SimpleDraweeView%20was%20not%20initialized!&oq=%25E6%2580%258E%25E4%25B9%2588%25E5%25BC%2595%25E5%2585%25A5com.facebook.drawee.backends.pipeline.Fresco&rsv_pq=cfac6ede0001ec1f&rsv_t=64d1qeXCZWpw1RVkNzSq10xtt8LINAjw0I5ctPFSkg7e85MsdWZbM%2B0gI2CSKuH1Y%2BJ77w&rqlang=cn&rsv_enter=1&inputT=8942&rsv_sug3=110&bs=%E6%80%8E%E4%B9%88%E5%BC%95%E5%85%A5com.facebook.drawee.backends.pipeline.Fresco"));
    }

    private void initTips() {
        for (int i=0;i<7;i++) {
            //todo 按照这个格式把有的数据填入,title,url：点击后跳转的url ->
            tips.add(new InterviewTipsItem("如何冷静面对面试官","https://baijiahao.baidu.com/s?id=1608574900105467287&wfr=spider&for=pc"));
        }
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
        mTvPagerTitle = view.findViewById(R.id.tv_pager_title);
        mLineLayoutDot = view.findViewById(R.id.lineLayout_dot);
        tipsList = (RecyclerView) view.findViewById(R.id.interviewer_tips);
        interviewTipsAdapter = new InterviewTipsAdapter(tips);
        tipsList.setAdapter(interviewTipsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        tipsList.setLayoutManager(linearLayoutManager);
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
                    //跳转到一个webview
                    Intent intent = new Intent(v.getContext(), ActivityWebView.class);
                    intent.putExtra("URL",url);
                    v.getContext().startActivity(intent);
                }
            });
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(mLineLayoutDot, fromResToDrawable(getContext(), R.drawable.ic_dot_focused), simpleDraweeViewList.size(),view);
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
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number,View view) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }

}
