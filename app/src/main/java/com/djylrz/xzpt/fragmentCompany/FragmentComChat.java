package com.djylrz.xzpt.fragmentCompany;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.DefaultMessagesActivity;
import com.djylrz.xzpt.bean.ChatUser;
import com.djylrz.xzpt.bean.Dialog;
import com.djylrz.xzpt.bean.DialogsFixtures;
import com.djylrz.xzpt.bean.Message;
import com.djylrz.xzpt.bean.User;
import com.djylrz.xzpt.utils.HttpUtil;
import com.djylrz.xzpt.utils.PostParameterName;
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

import com.vondear.rxtool.view.RxToast;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCOnlineStatusListener;
import com.xiaomi.mimc.MIMCServerAck;
import com.xiaomi.mimc.MIMCUser;
import com.xiaomi.mimc.common.MIMCConstant;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class FragmentComChat extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> ,UserManager.OnHandleMIMCMsgListener{
    private static final String TAG = "FragmentComChat";
    private View mDecorView;
    private DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDecorView = inflater.inflate(R.layout.fragment9_com_chat, container, false);
        dialogsList = (DialogsList) mDecorView.findViewById(R.id.dialogsList);
        initAdapter();
        // 设置处理MIMC消息监听器
        UserManager.getInstance().setHandleMIMCMsgListener(this);
        return mDecorView;
    }

    private void initAdapter() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Glide.with(getActivity()).load(R.drawable.avatar_default).into(imageView);
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
        //dialogsAdapter.setItems(DialogsFixtures.getDialogs());
        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);
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
        MIMCUser user = UserManager.getInstance().getUser();
        HttpUtil.getClient().removeAllHeaders();
        HttpUtil.getClient().addHeader("token",user.getToken());
        HttpUtil.getClient().addHeader("Content-Type","application/json");

        HttpUtil.get(PostParameterName.GET_URL_ALL_GET_CHAT_CONTENT_LIST, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content = new String(bytes);
                Log.d(TAG, "获取消息列表成功: " + content);
                //解析会话列表json
                ParseJson(content);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    @Override
    public void onDialogClick(Dialog dialog) {
        Toast.makeText(getContext(), "点击了消息项", Toast.LENGTH_SHORT).show();
        DefaultMessagesActivity.open(getContext());
        //onRefreshDialogList();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }
    /**
      *@Description: 解析返回的会话列表数据json
      *@Param: [json]
      *@Return: void
      *@Author: mingjun
      *@Date: 2019/5/22 下午 4:29
      */
    public void ParseJson(String json){
        GsonBuilder builder = new GsonBuilder();

        Gson gson =builder.create();
        Type jsonType = new TypeToken<ContactResponseData<List<Data<LastMessage>>>>() {}.getType();
        final ContactResponseData<List<Data<LastMessage>>> postResult = gson.fromJson(json, jsonType);
        Log.d(TAG, "onResponse: code"+postResult.getCode());
        if(postResult.getCode().equals(200)){
            List<Data<LastMessage>>  dataList = postResult.getData();
            for (int i = 0; i < dataList.size(); ++i) {
                Data<LastMessage> content = dataList.get(i);
                ArrayList<ChatUser> users = new ArrayList<>();
                ChatUser chatUser = new ChatUser(content.getLastMessage().getFromUuid(),content.getLastMessage().getFromAccount(),"",true);
                users.add(chatUser);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Message message = null;
                message = new Message(content.getLastMessage().getFromUuid(),chatUser,content.getLastMessage().getPayload(),new Date(Long.parseLong(content.getTimestamp())));
                Dialog dialog = new Dialog(content.getLastMessage().getSequence(),content.getLastMessage().getFromAccount(),"",users,message,1);
                dialogsAdapter.upsertItem(dialog);
            }
        }
    }

    /**
      *@Description: 处理单聊消息
      *@Param: [chatMsg]
      *@Return: void
      *@Author: mingjun
      *@Date: 2019/5/23 下午 5:42
      */
    @Override
    public void onHandleMessage(final ChatMsg chatMsg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//
//                mDatas.add(chatMsg);
//                mAdapter.notifyDataSetChanged();
//                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                ArrayList<ChatUser> users = new ArrayList<>();
                ChatUser chatUser = new ChatUser(chatMsg.getMsg().getMsgId(),chatMsg.getFromAccount(),"",true);
                users.add(chatUser);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Message message = null;
                message = new Message(chatMsg.getMsg().getMsgId(),chatUser,chatMsg.getMsg().getPayload().toString(),new Date(chatMsg.getMsg().getTimestamp()));
                Dialog dialog = new Dialog(chatMsg.getMsg().getMsgId(),chatMsg.getFromAccount(),"",users,message,1);
                dialogsAdapter.upsertItem(dialog);
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
                RxToast.info("聊天功能初始化成功->用户token为：" + UserManager.getInstance().getUser().getToken());            }
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

}
class Data<T>{
    /*
    {
          "userType":"USER",
          "id":"$uuid1",
          "name":"$appAccount1",
          "timestamp":"$ts3",
          "extra":"$extra",
          "lastMessage":{
              "fromUuid":"$fromUuid3",
              "fromAccount":"$fromAccount3",
              "payload":"$payload3", // 需base64解码
              "sequence":"$sequence3",
               "bizType":"$bizType"
          }
      }
      */
    private String userType;//当前用户uuid 开发者无需关心
    private String id;
    private String name;
    private String timestamp;//创建时间
    private String extra;//会话的扩展字段，用于实现一些自定义功能
    private T lastMessage ;
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public T getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(T lastMessage) {
        this.lastMessage = lastMessage;
    }

}
class LastMessage{
    private String fromUuid;
    private String fromAccount;
    private String payload;///消息体需base64解码
    private String sequence;//sequence主要用来做消息的排序和去重，全局唯一
    private String bizType;//可用于表示消息类型扩展字段（可选）

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}