package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.djylrz.xzpt.Activity.*;
import com.djylrz.xzpt.R;

import java.util.List;

public class UserInfoOptionAdapter extends RecyclerView.Adapter<UserInfoOptionAdapter.ViewHolder> {

    private List<UserSelector> mUserOptionList;


    public UserInfoOptionAdapter(List<UserSelector> userOptionList) {
        mUserOptionList = userOptionList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View userOptionView;
        ImageView userOptionImage;
        TextView userOptindescript;

        public ViewHolder(View v) {
            super(v);
            userOptionView = v;
            userOptionImage = (ImageView) v.findViewById(R.id.user_option_image);
            userOptindescript = (TextView) v.findViewById(R.id.user_option_descript);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_selector,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.userOptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                UserSelector userSelector = mUserOptionList.get(position);
                Toast.makeText(v.getContext(),"clicked" + userSelector.getOption(),Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent intent = new Intent(v.getContext(), MyResumeActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(v.getContext(), JobIntention.class);
                        v.getContext().startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(v.getContext(), FocusCompanyActivity.class);
                        v.getContext().startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(v.getContext(), HelpAndFeedbackActivity.class);
                        v.getContext().startActivity(intent3);
                        break;
                    case 4:
                        SharedPreferences userToken = v.getContext().getSharedPreferences("token",0);
                        SharedPreferences.Editor editor = userToken.edit();
                        editor.remove(PostParameterName.STUDENT_TOKEN);
                        editor.commit();
                        Intent intent4 = new Intent(v.getContext(), ActorChoose.class);
                        v.getContext().startActivity(intent4);
                        break;
                        default:
                            break;
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserSelector userSelector = mUserOptionList.get(position);
        holder.userOptionImage.setImageResource(userSelector.getImageId());
        holder.userOptindescript.setText(userSelector.getOption());
    }

    @Override
    public int getItemCount() {
        return mUserOptionList.size();
    }



}