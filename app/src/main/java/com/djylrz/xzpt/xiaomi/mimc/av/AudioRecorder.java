package com.djylrz.xzpt.xiaomi.mimc.av;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.djylrz.xzpt.xiaomi.mimc.listener.OnAudioCapturedListener;

/**
 * Created by houminjiang on 18-5-28.
 */

public class AudioRecorder implements Capture {
    private AudioCapture audioCapture;
    private Thread captureThread;
    private volatile boolean isExit = false;
    private boolean isCaptureStarted = false;
    private OnAudioCapturedListener onAudioCapturedListener;
    private final int MAX_BUFF_SIZE = 2 * 1024;
    private static final String TAG = "AudioRecorder";

    public void setOnAudioCapturedListener(OnAudioCapturedListener onAudioCapturedListener) {
        this.onAudioCapturedListener = onAudioCapturedListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean start() {
        if (isCaptureStarted) {
            Log.w(TAG, "Capture has already been started.");
            return false;
        }

        isExit = false;
        audioCapture = new AudioCapture();
        boolean result = audioCapture.start();
        if (result) {
            captureThread = new Thread(new AudioCaptureRunnable());
            captureThread.start();
            isCaptureStarted = true;
        }

        return result;
    }

    @Override
    public void stop() {
        if (!isCaptureStarted) {
            return;
        }
        isExit = true;
        try {
            captureThread.join(50);
            captureThread = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted exception:", e);
        }
        audioCapture.stop();
        isCaptureStarted = false;
    }

    private class AudioCaptureRunnable implements Runnable {
        @Override
        public void run() {
            while (!isExit) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                byte[] pcmData = new byte[MAX_BUFF_SIZE];
                int result = audioCapture.capture(pcmData, 0, MAX_BUFF_SIZE);
                if (result > 0) {
                    if (onAudioCapturedListener != null) {
                        onAudioCapturedListener.onAudioCaptured(pcmData);
                    }
                    //Log.d(TAG, String.format("Success captured " + result + "bytes. buffer size:%d", MAX_BUFF_SIZE));
                }
            }
            Log.i(TAG, "Audio capture thread exit.");
        }
    }
}
