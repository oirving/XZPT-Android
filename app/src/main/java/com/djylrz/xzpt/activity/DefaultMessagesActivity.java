package com.djylrz.xzpt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.MyApplication;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.ChatUser;
import com.djylrz.xzpt.bean.Message;
import com.djylrz.xzpt.utils.HttpUtil;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.bean.ChatMsg;
import com.djylrz.xzpt.xiaomi.mimc.bean.ContactResponseData;
import com.djylrz.xzpt.xiaomi.mimc.common.Constant;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.vondear.rxtool.view.RxToast;
import com.xiaomi.mimc.MIMCUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class DefaultMessagesActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener,
        UserManager.OnHandleMessageToMessageActivityListener {

    public static void open(Context context, String senderId, String userName, String headUrl) {
        context.startActivity(new Intent(context, DefaultMessagesActivity.class).putExtra("senderId", senderId)
                .putExtra("userName", userName)
                .putExtra("headUrl", headUrl));
    }

    private MessagesList messagesList;
    private static final int TOTAL_MESSAGES_COUNT = 100;

    protected String senderId = "0";
    protected String receiverId = "1";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private Toolbar toolbar;
    private String userName;
    private String headUrl;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        this.receiverId = getIntent().getStringExtra("senderId");
        this.userName = getIntent().getStringExtra("userName");
        this.headUrl = getIntent().getStringExtra("headUrl");
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.toolbar = findViewById(R.id.chat_with_user_message_toolbar);
        //设置标题栏
        toolbar.bringToFront();//标题栏置顶
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("与[" + userName + "]聊天");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                if (url == null || "".equals(url)) {
                    Glide.with(DefaultMessagesActivity.this).load(R.drawable.avatar_default).into(imageView);
                } else {
                    Glide.with(DefaultMessagesActivity.this).load(PostParameterName.HOST + "/file/" + url).into(imageView);
                }
            }
        };
        initAdapter();
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        input.setAttachmentsListener(this);
        UserManager.getInstance().setHandleMessageToMessageActivityListener(this);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        ChatUser chatUser = new ChatUser(senderId, "name", null, true);
        messagesAdapter.addToStart(new Message(senderId, chatUser, input.toString()), true);
        UserManager userManager = UserManager.getInstance();
        MIMCUser user = userManager.getUser();
        if (user != null) {
            userManager.sendMsg(receiverId, input.toString().getBytes(), Constant.TEXT);
        }
//        ChatUser chatUser2 = new ChatUser(receiverId,"name","avatar",true);
//        messagesAdapter.addToStart(new Message(receiverId,chatUser2,input.toString()),true);
        return true;
    }

    @Override
    public void onAddAttachments() {

    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
                        RxToast.info(message.getUser().getName() + " avatar click");
                    }
                });
        this.messagesList.setAdapter(messagesAdapter);
        onRefreshMessageList();
    }

    @Override
    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    @Override
    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }

    /**
     * @Description: 每次接收到单聊消息也会发送一份message给DefaultMessagesActivity
     * @Param: [chatMsg]
     * @Return: void
     * @Author: mingjun
     * @Date: 2019/5/25 下午 3:28
     */
    @Override
    public void onHandleMessage(final ChatMsg chatMsg, final String userName, final String headUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (receiverId.equals(chatMsg.getFromAccount())) {
                    ChatUser chatUser = new ChatUser(chatMsg.getFromAccount(), userName, headUrl, true);
                    messagesAdapter.addToStart(new Message(receiverId, chatUser, new String(chatMsg.getMsg().getPayload())), true);

                }
            }
        });
    }

    private void onRefreshMessageList() {
        //清空原有message列表数据
        messagesAdapter.clear();
        MIMCUser user = UserManager.getInstance().getUser();
        HttpUtil.getClient().removeAllHeaders();
        HttpUtil.getClient().addHeader("token", user.getToken());
        HttpUtil.getClient().addHeader("Content-Type", "application/json;charset=UTF-8");
        HttpUtil.getClient().addHeader("Accept", "application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toAccount", MyApplication.getUserId());
            jsonObject.put("fromAccount", receiverId);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            jsonObject.put("utcFromTime", cal.getTimeInMillis());
            cal.add(Calendar.DAY_OF_MONTH, +1);
            jsonObject.put("utcToTime", cal.getTimeInMillis());
            jsonObject.put("count", 20);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.post(PostParameterName.GET_URL_WEEK_GET_MESSAGE_LIST_BETWEEN_TWO_PERSON, jsonObject, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("json", "onFailure: " + errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("json", "onSuccess: " + response.toString());
                try {
                    parseJson(response.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseJson(String json) throws UnsupportedEncodingException {
        ArrayList<Message> messages = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type jsonType = new TypeToken<ContactResponseData<Data<List<MessageofData>>>>() {
        }.getType();
        final ContactResponseData<Data<List<MessageofData>>> postResult = gson.fromJson(json, jsonType);
        Log.d(MyApplication.TAG, "onResponse: code" + postResult.getCode());
        if (postResult.getCode().equals(200)) {
            List<MessageofData> messageofDataList = postResult.getData().getMessages();
            for (int i = 0; i < messageofDataList.size(); ++i) {
                MessageofData messageofData = messageofDataList.get(i);
                //需要对Payload进行base64解密
                ChatUser chatUser;
                if (messageofData.getFromAccount().equals(receiverId)) {
                    chatUser = new ChatUser(messageofData.getFromAccount(), userName, headUrl, true);
                } else {
                    chatUser = new ChatUser("0", "user", "avatar", true);
                }
                Log.d(MyApplication.TAG, "parseJson: :form:" + messageofData.getFromAccount() + "--> to:" + messageofData.getToAccount());
                Message message;
                //解析json消息体
                String payload = new String(android.util.Base64.decode(messageofData.getPayload(), android.util.Base64.DEFAULT));
                String regExp = "\"payload\":\"(.*)\"";
                Pattern pattern;
                Matcher matcher;
                pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(payload);
                if (matcher.find()) {
                    String lastMessageBase64 = matcher.group(1);
                    String lastMessage = new String(android.util.Base64.decode(lastMessageBase64, android.util.Base64.DEFAULT));
                    if (messageofData.getFromAccount().equals(receiverId)) {
                        message = new Message(messageofData.getFromAccount(), chatUser, new String(lastMessage));
                    } else {
                        message = new Message("0", chatUser, new String(lastMessage));
                    }
                } else {
                    message = new Message(messageofData.getFromAccount(), chatUser, "消息已损坏");
                }
                messages.add(message);
            }
        }
        messagesAdapter.addToEnd(messages, true);
    }
}

class Data<T> {
    /*
    "appId": $appId,
            "messages": [
    {
        "sequence": $sequence,
            "payload": $payload,
            "ts": $ts,
            "fromAccount":$fromAccount,
            "toAccount": $toAccount,
            "bizType":$bizType,
            "extra":$extra
    }
         ],
                 "row": $row,
            "timestamp":$timestamp
   */
    private String appId;
    private T messages;
    private String row;
    private String timestamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public T getMessages() {
        return messages;
    }

    public void setMessages(T messages) {
        this.messages = messages;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

class MessageofData {
    /*
                "messages": [
    {
        "sequence": $sequence,
            "payload": $payload,
            "ts": $ts,
            "fromAccount":$fromAccount,
            "toAccount": $toAccount,
            "bizType":$bizType,
            "extra":$extra
    }
         ],
         */
    private String sequence;
    private String payload;
    private String ts;
    private String fromAccount;
    private String toAccount;
    private String bizType;
    private String extra;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}