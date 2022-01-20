package com.android.attendme.Hardware;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.android.attendme.ml.MLHelpers;
import com.android.attendme.ml.OnFaceDetectionComplete;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MyCamera {
    //Camera utilities
    private Preview preview;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;
    private ImageAnalysis imageAnalysis;
    //utilities from activity
    private final Context appContext;
    private final Activity appActivity;
    //Executors
    private final ExecutorService imageAnalyserExe;
    //Logcat Tag
    private final String TAG = "Hardware.MyCamera";
    //CameraSelectors
    public static final int FRONT_CAMERA = CameraSelector.LENS_FACING_FRONT;
    public static final int REAR_CAMERA = CameraSelector.LENS_FACING_BACK;
    //Listeners and callbacks
    public interface OnStartCameraPreviewListener{
        default void onSuccess(){
        }
        void onFailure(String message);
    }
    public interface ImageAnalysisCallbacks{
        void onSuccess(Bitmap map);
        void onFailure(String message);
    }
    public MyCamera(Context context, Activity activity){
        appActivity = activity;
        appContext = context;
        imageAnalyserExe = Executors.newFixedThreadPool(1);
    }
    //Method to start camera, show the preview of the camera and create imageAnalysis
    public void startCameraForImageAnalysis(OnStartCameraPreviewListener listener,
                            PreviewView previewView,
                            int camera, Executor uiExecutor
                            ){
        if(!checkCameraPermission()) {
            listener.onFailure("Permission denied");
            return;
        }
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(appContext);
        cameraProviderFuture.addListener(()->{
            try {
                cameraProvider = cameraProviderFuture.get();
                preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(camera)
                        .build();
                imageAnalysis = new ImageAnalysis.Builder()
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                cameraProvider.bindToLifecycle((LifecycleOwner) appActivity,
                        cameraSelector,
                        preview,
                        imageAnalysis);
                listener.onSuccess();
            }catch (Exception e){
                listener.onFailure("Unable to start camera");
                Log.d(TAG, "Unable to create cameraProvider", e);
            }
        }, uiExecutor);
    }
    //Method to setImageAnalyser
    @SuppressLint("UnsafeOptInUsageError")
    public void setImageAnalyser(ImageAnalysisCallbacks callbacks, AssetManager assets){
        imageAnalysis.setAnalyzer(imageAnalyserExe, image -> {
            Image img = image.getImage();
            if(img != null) {
                Bitmap bitmapBuffer = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
                        Bitmap.Config.ARGB_8888);
                bitmapBuffer.copyPixelsFromBuffer(img.getPlanes()[0].getBuffer());
                Matrix mat = new Matrix();
                mat.setRotate(-90);
                Bitmap bitmap = Bitmap.createBitmap(bitmapBuffer,
                        0, 0,
                        bitmapBuffer.getWidth(), bitmapBuffer.getHeight(), mat, true);
                MLHelpers.Companion.detectFace(bitmap, assets, new OnFaceDetectionComplete() {
                    @Override
                    public void onFailure(@NonNull String message) {
                        callbacks.onFailure(message);
                    }

                    @Override
                    public void onSuccess(@NonNull Bitmap bitmap) {
                        callbacks.onSuccess(bitmap);
                    }
                });
                image.close();
            }else{
                callbacks.onFailure("Image is null");
                Log.d(TAG, "Image is null");
            }
        });
    }
    //Method to clear analyser
    public void clearImageAnalyser(){
        imageAnalysis.clearAnalyzer();
    }
    //Method to check camera permission
    private Boolean checkCameraPermission(){
        return (ContextCompat.checkSelfPermission(appContext,
                Manifest.permission.CAMERA
                )== PackageManager.PERMISSION_GRANTED);
    }
}
