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
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJQQVNTV09SRCI6IjM4M25tdmRwb25xYmlic2VpODkwcXZpbTVwMHIyajYiLCJVU0VSX0lEIjoiZTMzYWM1NjM0ZDRmNDBkYjk5ZDUzOTZlYjhkYjA1YjEiLCJleHAiOjE1NTg2MjQyMzF9.RNdDWQp0JfS1QNT6Skvjemxc9-JvfQxwZQfLWATTtrE";
        FileTransferUtil.getInstance().uploadFile(token, Environment.getExternalStorageDirectory().getAbsolutePath() + "/res", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               System.out.println(response.body().string());
            }
        });
    }
}
