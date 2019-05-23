package com.djylrz.xzpt.xiaomi.mimc.bean;


/**
  *@Description: 获取最近会话列表 json bean
  *@Author: mingjun
  *@Date: 2019/5/22 下午 3:19
  */
public class ContactResponseData<T> {
    private Integer code;
    private T data;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
      *@Description: 设置数据
      *@Param: [resultCode, resultMsg, t]
      *@Return: void
      *@Author: mingjun
      *@Date: 2019/5/22 下午 3:24
      */
    public void putData(Integer resultCode, String resultMsg, T t) {
        this.code = resultCode;
        this.message = resultMsg;
        this.data = t;
    }

    public ContactResponseData() {
        this.code = 200;
    }
}
