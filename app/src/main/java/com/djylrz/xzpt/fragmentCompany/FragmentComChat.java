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
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Glide.with(getActivity()).load(R.drawable.avatar_default).into(imageView);
            }
        };
        initAdapter();
        return mDecorView;
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(dialogsAdapter);
    }

    //for example
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

    @Override
    public void onDialogClick(Dialog dialog) {
        Toast.makeText(getContext(), "点击了消息项", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }
}
