package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.R;

public class ResumeItem {
    private String state;//投递状态
    private String position;//职位
    private String userName;//用户的名字
    private String time;//投递的时间
    int editImage;//编辑按钮
    int exportButton;//导出按钮

    public ResumeItem(String state,String position,String userName,String time) {
        this.state = state;
        this.position = position;
        this.userName = userName;
        this.time = time;
        this.editImage = R.drawable.edit;
        this.exportButton = R.id.resume_export_button;
    }

    public int getEditImage() {
        return editImage;
    }

    public int getExportButton() {
        return exportButton;
    }

    public String getPosition() {
        return position;
    }

    public String getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }
}
