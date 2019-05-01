package com.djylrz.xzpt.utils;

public class PostParameterName {
    public static final String HOST = "http://101.132.142.40:8080/XZPT-Java-1.0-SNAPSHOT";

    public static final String TOKEN = "token";

    public static final String POST_URL_LOGIN = HOST+"/user/login";//学生登录接口
    public static final String POST_URL_REGISTER = HOST+"/user/register?code=";//用户注册接口
    public static final String POST_URL_GETVERIFICATIONCODE = HOST+"/getverificationcode/";//获取验证码接口
    public static final String POST_URL_GET_USER_BY_TOKEN = HOST +"/user/getuserbytoken?token=";//查看用户信息
    public static final String POST_URL_UPDATE_USER_INRO = HOST + "/user/updateinfo?token=";//修改用户信息
    public static final String POST_URL_RESET_PASSWORD = HOST +"/user/resetpasswd?";//找回密码URL

    public static final String POST_URL_COMPANY_LOGIN = HOST + "/company/login/";//企业登录接口

    public static final String REQUEST_CODE = "code";
    public static final String REQUEST_EMAIL = "email";
    public static final String REQUEST_PASSWORD = "passwd";
    public static final String REQUEST_USERTYPE = "";


    public static final String RESPOND_RESULTCODE = "resultCode";
    public static final String RESPOND_RESULMSG = "resultMsg";
    public static final String RESPOND_RESULTOBJECT = "resultObject";

}
