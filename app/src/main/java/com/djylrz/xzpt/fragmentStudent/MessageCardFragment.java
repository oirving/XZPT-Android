package com.djylrz.xzpt.fragmentStudent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.DefaultMessagesActivity;
import com.djylrz.xzpt.bean.ChatUser;
import com.djylrz.xzpt.bean.Data;
import com.djylrz.xzpt.bean.Dialog;
import com.djylrz.xzpt.bean.LastMessage;
import com.djylrz.xzpt.bean.Message;
import com.djylrz.xzpt.bean.TempResponseData;
import com.djylrz.xzpt.utils.HttpUtil;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.bean.ChatDTO;
import com.djylrz.xzpt.xiaomi.mimc.bean.ChatMsg;
import com.djylrz.xzpt.xiaomi.mimc.bean.ContactResponseData;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.xiaomi.mimc.MIMCUser;
import com.xiaomi.mimc.common.MIMCConstant;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class MessageCardFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>,
        UserManager.OnHandleMIMCMsgListener ,
        DateFormatter.Formatter{
    private static final String TAG = "FragmentComChat";
    private View mDecorView;
    private DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;
    private HashMap<String, Integer> unReadMessageCountMap = new HashMap<>();
    private String userName;
    private String headUrl;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int finishCount = 0;
    private int dialogSize = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(++finishCount == dialogSize){
                        dialogsAdapter.sortByLastMessageDate();
                    }
                    break;
            }
        }
    };
    public static MessageCardFragment getInstance(String title) {
        MessageCardFragment mcf = new MessageCardFragment();
        return mcf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDecorView = inflater.inflate(R.layout.fragment_message_card, container, false);
        dialogsList = (DialogsList) mDecorView.findViewById(R.id.dialogsList_student);
        swipeRefreshLayout = mDecorView.findViewById(R.id.swipe_refresh_layout);
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                MIMCUser user = UserManager.getInstance().getUser();
                if (user != null) {
                    onRefreshDialogList();
                }
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
        initAdapter();
        // 设置处理MIMC消息监听器
        UserManager.getInstance().setHandleMIMCMsgListener(this);
        return mDecorView;
    }

    private void initAdapter() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                if (url == null || url.equals("")) {
                    Glide.with(getActivity()).load(R.drawable.avatar_default).into(imageView);
                } else {
                    Glide.with(getActivity()).load(PostParameterName.HOST + "/file/" + url).into(imageView);
                }
            }
        };
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        //设置列表数据
        /*
         adapter.setItems(List items) - replaces existing list with a new dialog list;
         adapter.addItems(List items) - adds a new dialog list to the end of the list;
         adapter.addItem(DIALOG dialog) - adds one dialog to the end of the list
         adapter.addItem(int position, DIALOG dialog) - adds a new dialog to the specified position.
         adapter.upsertItem(DIALOG dialog) - adds one dialog to the end of the list if not exists, otherwise updates the existing dialog.
         */
        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);
        dialogsAdapter.setDatesFormatter(this);
        dialogsList.setAdapter(dialogsAdapter);


    }

    //f如果对话框已更改，您可以通过调用按列表中的位置adapter.updateItem(int position, DIALOG item)更新它，
    // 或通过调用通过对话框ID更新它adapter.updateItemById(DIALOG item)
    private void onNewMessage(String dialogId, Message message) {
        boolean isUpdated = dialogsAdapter.updateDialogWithMessage(dialogId, message);
        if (!isUpdated) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
    }

    //for example
    private void onNewDialog(Dialog dialog) {
        dialogsAdapter.addItem(dialog);
    }
    //To delete messages from the list, you need to call "adapter.deleteById(String id)".
    //To delete all of the dialogs, just call "adapter.clear()" method.

    //for example
    private void onRefreshDialogList() {
        //清空原有会话列表数据
        dialogsAdapter.clear();
        finishCount = 0;
        MIMCUser user = UserManager.getInstance().getUser();
        HttpUtil.getClient().removeAllHeaders();
        HttpUtil.getClient().addHeader("token", user.getToken());
        HttpUtil.getClient().addHeader("Content-Type", "application/json");

        HttpUtil.get(PostParameterName.GET_URL_ALL_GET_CHAT_CONTENT_LIST, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content = new String(bytes);
                Log.d(TAG, "获取消息列表成功: " + content);
                //解析会话列表json
                try {
                    ParseJson(content);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MIMCUser user = UserManager.getInstance().getUser();
        if (user != null) {
            onRefreshDialogList();
        }
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        //Toast.makeText(getContext(), "点击了消息项", Toast.LENGTH_SHORT).show();
        DefaultMessagesActivity.open(getContext(), dialog.getId(), dialog.getUsers().get(0).getName(), dialog.getUsers().get(0).getAvatar());
        unReadMessageCountMap.put(dialog.getId(), 0);
        //onRefreshDialogList();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }

    /**
     * @Description: 解析返回的会话列表数据json
     * @Param: [json]
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/22 下午 4:29
     */
    public void ParseJson(String json) throws UnsupportedEncodingException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type jsonType = new TypeToken<ContactResponseData<List<Data<LastMessage>>>>() {
        }.getType();
        final ContactResponseData<List<Data<LastMessage>>> postResult = gson.fromJson(json, jsonType);
        Log.d(TAG, "onResponse: code" + postResult.getCode());
        if (postResult.getCode().equals(200)) {
            List<Data<LastMessage>> dataList = postResult.getData();
            dialogSize = dataList.size();
            for (int i = 0; i < dataList.size(); ++i) {
                final Data<LastMessage> dialogContent = dataList.get(i);
                final ArrayList<ChatUser> users = new ArrayList<>();
                final String fromAccount;
                if (dialogContent.getLastMessage().getFromAccount().equals(MyApplication.getUserId())) {
                    fromAccount = dialogContent.getLastMessage().getToAccount();
                } else {
                    fromAccount = dialogContent.getLastMessage().getFromAccount();
                }
                //获取头像和名称
                SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(PostParameterName.TOKEN, 0);
                String userToken = preferences.getString(PostParameterName.STUDENT_TOKEN, null);
                final String companyToken = preferences.getString(PostParameterName.TOKEN, null);
                String urlGetUserInfo;
                if (MyApplication.getUserType() == 1) {
                    urlGetUserInfo = PostParameterName.POST_URL_GET_USER_HEAD_NAME_BY_ID + userToken + "&userId=" + fromAccount + "&requestType=" + 1 + "&wantType=" + 0;
                } else {
                    urlGetUserInfo = PostParameterName.POST_URL_GET_USER_HEAD_NAME_BY_ID + companyToken + "&userId=" + fromAccount + "&requestType=" + 0 + "&wantType=" + 1;
                }
                Log.d(TAG, "addMsg: " + urlGetUserInfo);
                HttpUtil.post(urlGetUserInfo, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        String content = new String(bytes);
                        Log.d(TAG, "onSuccess: " + content);
                        Gson gson = new GsonBuilder().create();
                        Type jsonType = new TypeToken<TempResponseData<ChatDTO>>() {
                        }.getType();
                        try {
                            final TempResponseData<ChatDTO> postResult = gson.fromJson(content, jsonType);
                            if (postResult.getResultCode() == 200) {//获取成功
                                //Log.d(TAG, "onSuccess: name:" + postResult.getResultObject().getUserName() + "，headUrl:" + postResult.getResultObject().getHeadUrl());
                                userName = postResult.getResultObject().getUserName();
                                headUrl = postResult.getResultObject().getHeadUrl();
                            } else {
                                userName = "未知用户";
                                headUrl = "";
                            }
                        } catch (Exception e) {
                            userName = "未知用户";
                            headUrl = "";
                        }
                        ChatUser chatUser = new ChatUser(fromAccount, userName, headUrl, true);
                        users.add(chatUser);
                        Message message = null;
                        //需要对Payload进行base64解密
                        //解析json消息体
                        String payload = new String(android.util.Base64.decode(dialogContent.getLastMessage().getPayload(), android.util.Base64.DEFAULT));
                        Log.d(TAG, "ParseJson: " + payload);
                        Log.d(TAG, "unParseJson: " + dialogContent.getLastMessage().getPayload());

                        String regExp = "\"payload\":\"(.*)\"";
                        Pattern pattern;
                        Matcher matcher;
                        pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(payload);
                        if (matcher.find()) {
                            String lastMessageBase64 = matcher.group(1);
                            String lastMessage = new String(android.util.Base64.decode(lastMessageBase64, android.util.Base64.DEFAULT));
                            message = new Message(fromAccount, chatUser, new String(lastMessage), new Date(Long.parseLong(dialogContent.getTimestamp())));
                        } else {
                            message = new Message(fromAccount, chatUser, "消息已损坏", new Date(Long.parseLong(dialogContent.getTimestamp())));
                        }
                        int count = 0;
                        if (unReadMessageCountMap.get(fromAccount) != null) {
                            count = unReadMessageCountMap.get(fromAccount);
                        }
                        Dialog dialog = new Dialog(fromAccount, userName, headUrl, users, message, count);
                        dialogsAdapter.upsertItem(dialog);

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Log.d(TAG, "onFailure: 获取用户名称和头像失败");
                    }

                    @Override
                    public void onFinish() {
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        }
    }

    /**
     * @Description: 处理单聊消息
     * @Param: [chatMsg]
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/23 下午 5:42
     */
    @Override
    public void onHandleMessage(final ChatMsg chatMsg, final String userName, final String headUrl) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ChatUser> users = new ArrayList<>();
                ChatUser chatUser = new ChatUser(chatMsg.getMsg().getMsgId(), chatMsg.getFromAccount(), "", true);
                users.add(chatUser);
                Message message = null;
                message = new Message(chatMsg.getFromAccount(), chatUser, new String(chatMsg.getMsg().getPayload()), new Date(chatMsg.getMsg().getTimestamp()));
                if (unReadMessageCountMap.get(chatMsg.getFromAccount()) == null) {
                    unReadMessageCountMap.put(chatMsg.getFromAccount(), 1);
                } else {
                    unReadMessageCountMap.put(chatMsg.getFromAccount(), unReadMessageCountMap.get(chatMsg.getFromAccount()) + 1);
                }
                Dialog dialog = new Dialog(chatMsg.getFromAccount(), userName, headUrl, users, message, unReadMessageCountMap.get(chatMsg.getFromAccount()));

                dialogsAdapter.upsertItem(dialog);
                dialogsAdapter.updateItemById(dialog);
                Log.d(TAG, "receive new message: " + chatMsg.getFromAccount() + " " + chatMsg.getMsg().getMsgId());
            }
        });
    }

    @Override
    public void onHandleGroupMessage(ChatMsg chatMsg) {

    }


    //处理登录状态改变
    @Override
    public void onHandleStatusChanged(MIMCConstant.OnlineStatus status) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //刷新会话列表
                onRefreshDialogList();
                //RxToast.info("聊天功能初始化成功->学生token为：" + UserManager.getInstance().getUser().getToken());
            }
        });

    }

    @Override
    public void onHandleServerAck(MIMCServerAck serverAck) {

    }

    @Override
    public void onHandleCreateGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryGroupInfo(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryGroupsOfAccount(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleJoinGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQuitGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleKickGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleUpdateGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleDismissGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandlePullP2PHistory(String json, boolean isSuccess) {

    }

    @Override
    public void onHandlePullP2THistory(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleSendMessageTimeout(MIMCMessage message) {

    }

    @Override
    public void onHandleSendGroupMessageTimeout(MIMCGroupMessage groupMessage) {

    }

    @Override
    public void onHandleJoinUnlimitedGroup(long topicId, int code, String errMsg) {

    }

    @Override
    public void onHandleQuitUnlimitedGroup(long topicId, int code, String errMsg) {

    }

    @Override
    public void onHandleDismissUnlimitedGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroupMembers(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroups(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroupOnlineUsers(String json, boolean isSuccess) {

    }

    public interface messageCallBack {
        public void getNewMessage(Message message);
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            if(date.getHours()>=12){
                return "下午 " + sdf.format(date);
            }else{
                return "上午 " + sdf.format(date);
            }
        }else if (DateFormatter.isYesterday(date)) {
            return "昨天";
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
}