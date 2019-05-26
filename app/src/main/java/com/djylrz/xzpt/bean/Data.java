package com.djylrz.xzpt.bean;

public class Data<T> {
    /*
    {
          "userType":"USER",
          "id":"$uuid1",
          "name":"$appAccount1",
          "timestamp":"$ts3",
          "extra":"$extra",
          "lastMessage":{
              "fromUuid":"$fromUuid3",
              "fromAccount":"$fromAccount3",
              "payload":"$payload3", // 需base64解码
              "sequence":"$sequence3",
               "bizType":"$bizType"
          }
      }
      */
    private String userType;//当前用户uuid 开发者无需关心
    private String id;
    private String name;
    private String timestamp;//创建时间
    private String extra;//会话的扩展字段，用于实现一些自定义功能
    private T lastMessage;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public T getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(T lastMessage) {
        this.lastMessage = lastMessage;
    }

}
