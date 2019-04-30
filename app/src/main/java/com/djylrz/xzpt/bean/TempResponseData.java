package com.djylrz.xzpt.bean;


/**
 * @author Murphy
 * @date 2019/4/26 20:52
 *
 * 这个文件在客户端为临时文件，主要是为了配合HttpClient的使用，//TODO 可删除。
 */
public class TempResponseData<T> {
    private Integer resultCode;
    private String resultMsg;
    private T resultObject;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultObject() {
        return resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }

    /**
     * @param resultCode
     * @param resultMsg
     * @param t
     * @return void
     * @author Murphy
     * @date 2019/4/26 21:17
     * @description 设置数据
     */
    public void putData(Integer resultCode, String resultMsg, T t) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultObject = t;
    }

    public TempResponseData() {
        this.resultCode = 200;
    }
}
