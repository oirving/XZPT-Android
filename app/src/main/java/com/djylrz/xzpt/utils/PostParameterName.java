package com.djylrz.xzpt.utils;

public class PostParameterName {
    public static final String HOST = "http://101.132.142.40:8080/XZPT-Java-1.0-SNAPSHOT";

    public static final String TOKEN = "token";//token，企业token，
    public static final String STUDENT_TOKEN = "student_token";//学生token
    public static final String STUDENT_USER_JSON = "student_user_json";//登录之后获取用户信息存到本地SharedPreferences的key

    public static final String FILE = "file";//登录之后获取用户信息存到本地SharedPreferences的key

    public static final String POST_URL_GET_USER_HEAD_NAME_BY_ID = HOST + "/chat/getuserinfo?token=";//根据用户id获取头像和用户名

    public static final String POST_URL_LOGIN = HOST+"/user/login";//学生登录接口
    public static final String POST_URL_LOGIN_WITH_TOKEN = HOST + "/user/vertifytoken?token=";//学生token登录
    public static final String POST_URL_REGISTER = HOST+"/user/register?code=";//用户注册接口
    public static final String POST_URL_GETVERIFICATIONCODE = HOST+"/getverificationcode/";//获取验证码接口
    public static final String POST_URL_GET_USER_BY_TOKEN = HOST +"/user/getuserbytoken?token=";//查看用户信息
    public static final String POST_URL_UPDATE_USER_INRO = HOST + "/user/updateinfo?token=";//修改用户信息
    public static final String POST_URL_RESET_PASSWORD = HOST +"/user/resetpasswd?";//找回密码URL
    public static final String POST_URL_USER_GET_RECRUITMENT = HOST + "/user/getrecruitment?token="; // 查看招聘信息

    public static final String POST_URL_GET_RECOMMEND = HOST +"/user/getrecommend?token=";//获取推荐信息URL
    public static final String POST_URL_SEARCH_RECRUIMENT=HOST +"/user/searchrecruitment?token=";//&keyWord=  搜索招聘信息
    public static final String POST_URL_GET_HOT_RECRUIMENT=HOST +"/user/gethotspot?token=";//&keyWord=  获取热度招聘信息


    public static final String POST_URL_GET_RESUME_TEMPLATE = HOST +"/getlistresumetemplate";//获取简历模版
    public static final String POST_URL_CREATE_RESUME = HOST + "/user/createresume?token=";//创建简历接口
    public static final String POST_URL_GET_LIST_RESUME = HOST + "/user/getlistresume?token=";//获取所有简历接口
    public static final String POST_URL_UPDATE_RESUME = HOST + "/user/updateresume?token=";//修改简历接口
    public static final String POST_URL_DELETE_RESUME = HOST + "/user/deleteresume?token=";//删除简历接口
    public static final String POST_URL_GET_RESUME = HOST + "/user/getresume?token=";//查看单个简历接口

    public static final String POST_URL_DELIVER_RESUME = HOST + "/user/deliveryresume?token=";//投递简历接口


    public static final String POST_URL_COMPANY_LOGIN = HOST + "/company/login/";//企业登录接口
    public static final String POST_URL_COMPANY_LOGIN_WITH_TOKEN = HOST + "/company/vertifytoken?token=";//企业token登录
    public static final String POST_URL_COMPANY_RELEASE_RECRUITMENT = HOST + "/company/releaserecruitment?token=";//企业token登录
    public static final String POST_URL_GET_COMPANY_BY_TOKEN = HOST +"/company/getcompanybytoken?token=";//查看企业信息
    public static final String POST_URL_COMPANY_GET_RECRUITMENT_LIST = HOST + "/company/getlistrecruitment?token=";//企业查看所有招聘信息（分页）
    public static final String POST_URL_COMPANY_GET_DELIVER_RECORD = HOST + "/company/getlistdeliveryrecord?token=";//企业查看所有招聘信息（分页）
    public static final String POST_URL_COMPANY_GET_RESUME_BY_ID = HOST + "/company/getresumebyid?token=";//企业查看用户简历
    public static final String POST_URL_COMPANY_UPDATE_DELIVERY_RECORD = HOST + "/company/updatedeliveryrecord?token=";//企业修改用户简历投递记录
    public static final String POST_URL_COMPANY_UPDATE_RESRUITMENT = HOST + "/company/updaterecruitment?token=";//企业修改招聘信息
    public static final String POST_URL_COMPANY_GET_RESRUITMENT = HOST + "/company/getrecruitment?token=";//企业获取单条招聘信息
    public static final String POST_URL_COMPANY_DELETE_RECRUITMENT = HOST + "/company/deleterecruitment?token=";//企业删除单条招聘信息
    public static final String POST_URL_COMPANY_IMPORT_RECRUITMENT_LIST_BY_FILE = HOST + "/company/importdata?token=";//企业导入信息，从文件导入
    public static final String GET_URL_ALL_GET_CHAT_CONTENT_LIST = "https://mimc.chat.xiaomi.net/api/contact/";//用户/企业 获取历史消息列表
    public static final String GET_URL_WEEK_GET_MESSAGE_LIST_BETWEEN_TWO_PERSON = "https://mimc.chat.xiaomi.net/api/msg/p2p/queryOnCount/";//用户/企业 获取两个人之间的指定数量的历史消息


    public static final String REQUEST_CODE = "code";
    public static final String REQUEST_EMAIL = "email";
    public static final String REQUEST_PASSWORD = "passwd";
    public static final String REQUEST_USERTYPE = "";
    public static final String REQUEST_KEYWORD = "keyWord";
    public static final String REQUEST_RESUME_ID = "resumeId";
    public static final String REQUEST_RECRUITMENT_ID = "recruitmentId";

    public static final String RESPOND_RESULTCODE = "resultCode";
    public static final String RESPOND_RESULMSG = "resultMsg";
    public static final String RESPOND_RESULTOBJECT = "resultObject";


    /**
     * 文件相关，代表文件是否为私密
     */
    public static final String IS_PRIVATE = "private";

    public static final int CHOOSE_RESUME_TO_DELIVER = 1;//Activity之间选择简历用于投递的跳转的结果

    /*下载拼接链接*/
    public static final String DOWNLOAD_URL_RESUME_IMAGE_PREFIX = HOST+"/file/";//简历模板缩略图
    /*intent之间传递简历模板的key*/
    public static final String INTENT_PUT_EXTRA_KEY_RESUME_TEMPLATE_FILENAME = "filename";
}
