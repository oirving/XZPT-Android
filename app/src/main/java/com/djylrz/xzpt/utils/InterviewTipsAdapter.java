package com.djylrz.xzpt.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activity.ActivityWebView;

import java.util.List;



public class InterviewTipsAdapter extends RecyclerView.Adapter<InterviewTipsAdapter.ViewHolder> {

    private List<InterviewTipsItem> interviewTipsItems ;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View tipsListView;
        TextView title;//标题

        public ViewHolder(View view) {
            super(view);
            tipsListView = view;
            title = (TextView) view.findViewById(R.id.interview_title);
        }
    }

    public InterviewTipsAdapter (List<InterviewTipsItem> interviewTipsItems){
        this.interviewTipsItems = interviewTipsItems;
    }
    @NonNull
    @Override
    public InterviewTipsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.interview_item,viewGroup,false);
        final ViewHolder holder = new InterviewTipsAdapter.ViewHolder(view);
        holder.tipsListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                InterviewTipsItem interviewTipsItem = interviewTipsItems.get(position);
                String url = interviewTipsItem.getUrl();
                Intent intent = new Intent(v.getContext(), ActivityWebView.class);
                intent.putExtra("URL",url);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder  viewHolder, int position) {
        InterviewTipsItem interviewTipsItem = interviewTipsItems.get(position);
        viewHolder.title.setText(interviewTipsItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return interviewTipsItems.size();
    }
}
