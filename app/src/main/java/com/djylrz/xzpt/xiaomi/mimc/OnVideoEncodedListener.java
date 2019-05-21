package com.djylrz.xzpt.xiaomi.mimc;

/**
 * Created by houminjiang on 18-6-14.
 */

public interface OnVideoEncodedListener {
    void onVideoEncoded(byte[] data, int width, int height, long sequence);
}
