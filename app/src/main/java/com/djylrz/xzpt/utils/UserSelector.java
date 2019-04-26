package com.djylrz.xzpt.utils;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author oirving
 * @description
 */

public class UserSelector {

    private int imageId;

    private String option;


    /**
     *
     * @param imageId 图标
     * @param option 选项描述
     *
     */
    public UserSelector(int imageId,String option) {
        this.imageId = imageId;
        this.option = option;
    }

    public String getOption() {
        return  this.option;
    }

    public int getImageId() {
        return  this.imageId;
    }

}
