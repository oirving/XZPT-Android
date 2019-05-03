package com.djylrz.xzpt.utils;

public class PostParameterName {
    public static final String HOST = "http://101.132.142.40:8080/XZPT-Java-1.0-SNAPSHOT";

    public static final String TOKEN = "token";//token，企业token，
    public static final String STUDENT_TOKEN = "student_token";//学生token
    public static final String STUDENT_USER_JSON = "student_user_json";//登录之后获取用户信息存到本地SharedPreferences的key


    public static final String POST_URL_LOGIN = HOST+"/user/login";//学生登录接口
    public static final String POST_URL_LOGIN_WITH_TOKEN = HOST + "/user/vertifytoken?token=";//学生token登录
    public static final String POST_URL_REGISTER = HOST+"/user/register?code=";//用户注册接口
    public static final String POST_URL_GETVERIFICATIONCODE = HOST+"/getverificationcode/";//获取验证码接口
    public static final String POST_URL_GET_USER_BY_TOKEN = HOST +"/user/getuserbytoken?token=";//查看用户信息
    public static final String POST_URL_UPDATE_USER_INRO = HOST + "/user/updateinfo?token=";//修改用户信息
    public static final String POST_URL_RESET_PASSWORD = HOST +"/user/resetpasswd?";//找回密码URL
    public static final String POST_URL_USER_GET_RECRUITMENT = HOST + "/user/getrecruitment"; // 查看招聘信息

    public static final String POST_URL_GET_RECOMMEND = HOST +"/user/getrecommend?token=";//获取推荐信息URL
    public static final String POST_URL_SEARCH_RECRUIMENT=HOST +"/user/searchrecruitment?token=";//&keyWord=  搜索招聘信息

    public static final String POST_URL_CREATE_RESUME = HOST + "/user/createresume?token=";//创建简历接口


    public static final String POST_URL_COMPANY_LOGIN = HOST + "/company/login/";//企业登录接口
    public static final String POST_URL_COMPANY_LOGIN_WITH_TOKEN = HOST + "/company/vertifytoken?token=";//企业token登录
    public static final String POST_URL_COMPANY_RELEASE_RECRUITMENT = HOST + "/company/releaserecruitment?token=";//企业token登录
    public static final String POST_URL_COMPANY_GET_RECRUITMENT_LIST = HOST + "/company/getlistrecruitment?token=";//企业查看所有招聘信息（分页）
    public static final String POST_URL_COMPANY_GET_DELIVERT_RECORD = HOST + "/company/getlistdeliveryrecord?token=";//企业查看所有招聘信息（分页）
    public static final String POST_URL_COMPANY_GET_RESUME_BY_ID = HOST + "/company/getresumebyid?token=";//企业查看用户简历


    public static final String REQUEST_CODE = "code";
    public static final String REQUEST_EMAIL = "email";
    public static final String REQUEST_PASSWORD = "passwd";
    public static final String REQUEST_USERTYPE = "";
    public static final String REQUEST_KEYWORD = "keyWord";

    public static final String RESPOND_RESULTCODE = "resultCode";
    public static final String RESPOND_RESULMSG = "resultMsg";
    public static final String RESPOND_RESULTOBJECT = "resultObject";

}
