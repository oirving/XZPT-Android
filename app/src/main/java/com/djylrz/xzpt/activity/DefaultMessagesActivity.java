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
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.ChatUser;
import com.djylrz.xzpt.bean.Message;
import com.djylrz.xzpt.bean.MessagesFixtures;
import com.djylrz.xzpt.utils.PostParameterName;
import com.djylrz.xzpt.xiaomi.mimc.bean.ChatMsg;
import com.djylrz.xzpt.xiaomi.mimc.common.UserManager;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.vondear.rxtool.view.RxToast;

import java.util.Date;

public class DefaultMessagesActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener,
        UserManager.OnHandleMessageToMessageActivityListener {

    public static void open(Context context, String senderId) {
        context.startActivity(new Intent(context, DefaultMessagesActivity.class).putExtra("senderId", senderId));
    }

    private MessagesList messagesList;
    private static final int TOTAL_MESSAGES_COUNT = 100;

    protected String senderId = "0";
    protected String receiverId = "1";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private Toolbar toolbar;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        this.receiverId = getIntent().getStringExtra("senderId");
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.toolbar = findViewById(R.id.chat_with_user_message_toolbar);
        //设置标题栏
        toolbar.bringToFront();//标题栏置顶
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("聊天");
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
                if(url == null || url.equals("")){
                    Glide.with(DefaultMessagesActivity.this).load(R.drawable.avatar_default).into(imageView);
                }else{
                    Glide.with(DefaultMessagesActivity.this).load(PostParameterName.HOST+"/file/"+url).into(imageView);
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
        ChatUser chatUser = new ChatUser(senderId, "name", "avatar", true);
        messagesAdapter.addToStart(new Message(senderId, chatUser, input.toString()), true);
//        ChatUser chatUser2 = new ChatUser(receiverId,"name","avatar",true);
//        messagesAdapter.addToStart(new Message(receiverId,chatUser2,input.toString()),true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
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
}
