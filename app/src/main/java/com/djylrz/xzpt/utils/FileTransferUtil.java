package com.djylrz.xzpt.utils;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 文件上传相关Util
 *
 * @author Murphy
 * @date 2019/5/18 19:31
 */
public class FileTransferUtil {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("multipart/form-data");
    private static final String UPLOAD_URL = "/upload";
    private static volatile FileTransferUtil instance = null;

    private FileTransferUtil() {
    }

    public static FileTransferUtil getInstance() {
        if (instance == null) {
            synchronized (FileTransferUtil.class) {
                if (instance == null) {
                    instance = new FileTransferUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @Description: 上传文件接口，因为Volley不会用，所以直接使用OKHTTP上传
     * @Created in: 2019/5/18
     * @Author: Murphy
     * @Method_Name:uploadFile
     * @Param: token 上传用户的token
     * @Param: filePath 上传的文件路径
     * @Param: callback 回调函数
     * @Param: isPrivate 上传文件是否为私密文件
     * @Return:void
     **/
    public void uploadFile(String token, String filePath, Callback callback, boolean isPrivate) {
        // 获得输入框中的路径
        String path = filePath.trim();
        File file = new File(path);
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(PostParameterName.TOKEN, token) // 提交普通字段
                .addFormDataPart(PostParameterName.IS_PRIVATE, (isPrivate ? 1 : 0) + "") // 提交普通字段
                .addFormDataPart(PostParameterName.FILE, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .build();

        Request request = new Request.Builder()
                .url(PostParameterName.HOST + UPLOAD_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
