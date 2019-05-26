package com.djylrz.xzpt.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.djylrz.xzpt.R;
import com.djylrz.xzpt.activityStudent.IntentResumeFileActivity;
import com.djylrz.xzpt.activityStudent.ResumeDisplayActivity;

import java.util.List;
public class ResumeModelListAdapter extends RecyclerView.Adapter<ResumeModelListAdapter.ViewHolder> {

    private List<ResumeModelItem> mResumeModelList;
    //todo 填写文件名，会在文件浏览顶部显示 ->小榕
    private String fileName="TBS测试.doc";
    //    private String fileUrl="http://123.207.239.170/test.docx";//远程文档地址，如下载失败请验证此链接是否还可用（那个时候可能我养不住服务器了）
    //todo 填写文档的地址 ->小榕
    private String fileUrl="http://www.fuzhou.gov.cn/tzgg/201904/P020190430575112821124.docx";//远程文档地址

    public ResumeModelListAdapter(List<ResumeModelItem> resumeModelList) {
        mResumeModelList = resumeModelList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View resumeModelListView;
        ImageView resumeModel;

        public ViewHolder(View v) {
            super(v);
            resumeModelListView = v;
            resumeModel = (ImageView) v.findViewById(R.id.resume_preview_imageview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resume_model_item,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.resumeModelListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResumeModelItem resumeModelItem = mResumeModelList.get(position);
                Intent intent = new Intent(v.getContext(),IntentResumeFileActivity.class);
                intent.putExtra(PostParameterName.INTENT_PUT_EXTRA_KEY_RESUME_TEMPLATE_FILENAME,resumeModelItem.getResumeTemplate().getTemplateFileName());
                v.getContext().startActivity(intent);
                //动态权限申请
//                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
//                } else {
//                    ResumeDisplayActivity.actionStart(v.getContext(),fileUrl,fileName);
//                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumeModelItem resumeModelItem = mResumeModelList.get(position);
        //holder.resumeModel.setImageResource(resumeModelItem.getResumeModel());
        if (resumeModelItem.getResumeTemplate()!=null) {
            Uri imageUri = Uri.parse(PostParameterName.DOWNLOAD_URL_RESUME_IMAGE_PREFIX + resumeModelItem.getResumeTemplate().getImgFileName());
            Glide.with(holder.resumeModelListView.getContext()).load(imageUri).into(holder.resumeModel);
        }
    }

    @Override
    public int getItemCount() {
        return mResumeModelList.size();
    }

}