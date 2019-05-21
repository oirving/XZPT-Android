package com.djylrz.xzpt.fragmentCompany;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djylrz.xzpt.R;

import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;


public class FragmentComChat extends Fragment {
    private View mDecorView;
    private MsgListAdapter mAdapter;
    private MessageList messageList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDecorView = inflater.inflate(R.layout.fragment9_com_chat,container,false);

        //列表
        messageList = (MessageList) mDecorView.findViewById(R.id.msg_list);
        //设置接收方或者发送方显示昵称
        messageList.setShowSenderDisplayName(true);
        messageList.setShowReceiverDisplayName(true);

        //设置禁止下拉刷新
//        messageList.forbidScrollToRefresh(true);

        //添加 Header View，并实现下拉刷新接口
        final PullToRefreshLayout ptrLayout = (PullToRefreshLayout) mDecorView.findViewById(R.id.pull_to_refresh_layout);
        PtrDefaultHeader header = new PtrDefaultHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PullToRefreshLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(getContext(),15), 0,DisplayUtil.dp2px(getContext(),10));
        header.setPtrFrameLayout(ptrLayout);
        ptrLayout.setLoadingMinTime(1000);
        ptrLayout.setDurationToCloseHeader(1500);
        ptrLayout.setHeaderView(header);
        ptrLayout.addPtrUIHandler(header);
        // 如果设置为 true，下拉刷新时，内容固定，只有 Header 变化
        ptrLayout.setPinContent(true);
        ptrLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("MessageListActivity", "Loading next page");
                loadMessageList();
                // 加载完历史消息后调用
                ptrLayout.refreshComplete();
            }
        });

        return mDecorView;
    }
    public void loadMessageList(){
        // 在消息列表顶部插入消息，参数列表按照时间顺序排序(最后一条消息是最新的)。
    }


}
