package com.android.attendme.Hardware;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

import androidx.appcompat.app.AppCompatActivity;

public final class MyVibrator {

    private final Context context;

    public MyVibrator(Context context){
        this.context = context;
    }

    public void buttonClickVibration(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createOneShot(20, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager manager = (VibratorManager)
                        context.getSystemService(AppCompatActivity.VIBRATOR_MANAGER_SERVICE);
                manager.getDefaultVibrator().vibrate(effect);
            } else {
                Vibrator vibrator = (Vibrator)
                        context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE);
                vibrator.vibrate(effect);
            }
        }

    }
}
