package com.djylrz.xzpt.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int SEX_MALE = 1 ;
    public static final int SEX_FEMALE = 2;
    public static final int SEX_DEFAULT = 0;

    public static final int EDUCATION_LEVEL_DEFAULT = 1;
    public static final int EDUCATION_LEVEL_JUNIOR_COLLEGE = 2;
    public static final int EDUCATIONAL_LEVEL_BACHELOR = 3;
    public static final int EDUCATIONAL_LEVEL_MASTER = 4;
    public static final int EDUCATIONAL_LEVEL_DOCTOR = 5;

    public static final int WORK_TIME_NINE_DEFAULT = 0;
    public static final int WORK_TIME_NINE_FIVE_FIVE = 1;
    public static final int WORK_TIME_NINE_SIX_FIVE = 2;
    public static final int WORK_TIME_NINE_FIVE_SIX = 3;
    public static final int WORK_TIME_NINE_NINE_SIX = 4;

    public static final int JOB_TYPE_DEFAULT = 0;
    public static final int JOB_TYPE_INTERN = 1;
    public static final int JOB_TYPE_PART_TIME = 2;
    public static final int JOB_TYPE_FULL_TIME = 3;

    public static final String[] SEX = {"默认","男","女"};
    public static final String[] EDUCATION_LEVEL = {"其他","大专","本科","硕士","博士"};
    public static final String[] INDUSTRY_LABEL = {"默认", "测试|开发|运维类", "产品|需求|项目类", "运营|编辑|客服类", "市场|商务类", "销售类", "综合职能|高级管理", "金融类", "文娱|传媒|艺术|体育", "教育|培训", "商业服务|专业服务", "贸易|批发|零售|租赁业", "交通|运输|物流|仓储", "房地产|建筑|物业", "生产|加工|制造", "能源矿产|农林牧渔", "化工|生物|制药|医护", "公务员|其他"};
    public static final String[] WORK_TIME =  {"默认","955","965","956","996"};

    public static final String[] RESUME_STATE = {"拒绝","未投递","已投递","已查看", "面试待安排", "一面", "二面", "终面", "通过"};//获取到的简历状态+1，RESUME_STATE[state+1],因为拒绝为-1
    public static final int RESUME_STATE_REJECTED = -1;
    public static final int RESUME_STATE_NOT_DELIVERED = 0;
    public static final int RESUME_STATE_DELIVERED = 1;
    public static final int RESUME_STATE_CHECKED = 2;
    public static final int RESUME_STATE_WAIT_INTERVIEW = 3;
    public static final int RESUME_STATE_FIRST_INTERVIEW = 4;
    public static final int RESUME_STATE_SECOND_INTERVIEW = 5;
    public static final int RESUME_STATE_FINAL_INTERVIEW = 6;
    public static final int RESUME_STATE_PASS = 7;

    //标题栏文字
    public static final String CREATE_RESUME = "创建简历";
    public static final String EDIT_RESUME = "编辑简历";
    //区分是创建新简历还是编辑已有简历的intent的key
    public static final String INTENT_PUT_EXTRA_KEY_CREATE_OR_EDIT_RESUME = "createOrEdit";
    //保存编辑或创建
    public static final String EDIT_OR_CREATE_RESUME_SAVE_BUTTON = "保存";
    public static final String USER_MANUAL_URL = "https://serve.wangmingjun.top/app/xzpt/user_manual.html";
    public static final String USER_ABOUT_ME = "https://serve.wangmingjun.top/app/xzpt/about_me.html";

}
