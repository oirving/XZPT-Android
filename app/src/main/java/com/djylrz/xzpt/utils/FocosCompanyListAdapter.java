package com.djylrz.xzpt.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.djylrz.xzpt.R;

import java.util.List;

public class FocosCompanyListAdapter extends RecyclerView.Adapter<FocosCompanyListAdapter.ViewHolder> {
    private List<FocusCompanyItem> mFocusCompanyItems;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView companyLogo;
        TextView companyName;

        public ViewHolder(View view) {
            super(view);
            companyLogo = (ImageView) view.findViewById(R.id.focus_company_logo_image);
            companyName = (TextView) view.findViewById(R.id.focus_company_name_textview);
        }
    }

    public FocosCompanyListAdapter(List<FocusCompanyItem> focusCompanyItems){
        mFocusCompanyItems = focusCompanyItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.focuscompany_items,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FocusCompanyItem focusCompanyItem = mFocusCompanyItems.get(position);
        viewHolder.companyLogo.setImageResource(focusCompanyItem.getCompanyLoge());
        viewHolder.companyName.setText(focusCompanyItem.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return mFocusCompanyItems.size();
    }
}
