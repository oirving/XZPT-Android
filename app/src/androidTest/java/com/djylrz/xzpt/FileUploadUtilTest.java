package com.djylrz.xzpt;

import android.os.Environment;
import android.util.Log;

import com.djylrz.xzpt.utils.FileTransferUtil;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;

public class FileUploadUtilTest {
    @Test
    public void uploadTest() throws IOException {
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJQQVNTV09SRCI6ImE3bGNkZDNoa2EyZDZkMHRoZzY2N2s3aGthMzJjYWdvIiwiVVNFUl9JRCI6ImI0N2MwOWY0OWQyZTQ4MmJiYzNkNjA2ZWVkNmUxOGM2IiwiZXhwIjoxNTU5ODA3MDEzfQ.uAmWi-QqF_YbqUw5LERuH0vHfWUL9XK1mvvI7HOo1sI";
        FileTransferUtil.getInstance().uploadFile(token, Environment.getExternalStorageDirectory().getAbsolutePath() + "/res", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO 失败处理
                System.out.println("失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO 成功处理
                System.out.println("成功");
            }
        }, true);
    }
}
