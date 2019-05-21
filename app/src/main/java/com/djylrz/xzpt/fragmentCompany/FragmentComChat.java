package com.djylrz.xzpt.fragmentCompany;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Dialog;
import com.djylrz.xzpt.bean.DialogsFixtures;
import com.djylrz.xzpt.bean.Message;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;


public class FragmentComChat extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {
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
        dialogsAdapter.setItems(DialogsFixtures.getDialogs());

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

    @Override
    public void onDialogClick(Dialog dialog) {
        Toast.makeText(getContext(), "点击了消息项", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }
}
