package com.android.attendme.EnvironmentOps;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public final class EnvironmentOps {
    //TAG
    private final static String TAG = "EnvironmentOps";

    //Method to force Garbage collector to run after Face registration over
    public static void fireGCAfterFaceRegistration(){
        try {
            Thread finalGcThread = new Thread(() -> {
                Looper.prepare();
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    Runtime.getRuntime().gc();
                }, 30000);
                Looper.loop();
            });
            new Thread(() -> {
                Looper.prepare();
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    Runtime.getRuntime().gc();
                }, 30000);
                Looper.loop();
                finalGcThread.start();
            }).start();
        }catch (Exception e){
            Log.d(TAG, "Exception in fireGCAfterFaceRegistration", e);
        }
    }

    //Method to schedule garbage collector task after imageAnalysis
    public static void gcAfterCameraImageAnalysis(){
        System.gc();
    }
}
