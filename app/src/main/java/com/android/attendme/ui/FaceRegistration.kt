package com.android.attendme.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.android.attendme.EnvironmentOps.EnvironmentOps
import com.android.attendme.Hardware.MyCamera
import com.android.attendme.Helpers.Helpers
import com.android.attendme.R
import com.android.attendme.ui.dialogBoxes.ErrorDialog
import com.android.attendme.ui.dialogBoxes.WarningDialog


class FaceRegistration : AppCompatActivity() {

    private lateinit var previewView : PreviewView
    private lateinit var tickAnimator : LottieAnimationView
    private var startingTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_registration)
        previewView = findViewById(R.id.Preview_surface)
        tickAnimator = findViewById(R.id.lottie_tick_anime)
        val myCamera = MyCamera(applicationContext, this@FaceRegistration)
        myCamera.startCameraForImageAnalysis(object : MyCamera.OnStartCameraPreviewListener{
            override fun onFailure(message: String?) {
                runOnUiThread {
                    Helpers.makeToast(applicationContext, message)
                }
            }

            override fun onSuccess() {
                super.onSuccess()
                myCamera.setImageAnalyser(object : MyCamera.ImageAnalysisCallbacks{
                    override fun onSuccess(map: Bitmap?) {
                        runOnUiThread {
                            myCamera.clearImageAnalyser()
                            previewView.visibility = View.GONE
                            tickAnimator.visibility = View.VISIBLE
                            tickAnimator.playAnimation()
                            EnvironmentOps.fireGCAfterFaceRegistration()
                            //Store bitmap
                            val warningDialog = WarningDialog()
                            warningDialog.showDialog(this@FaceRegistration, "No warning")
                        }
                    }

                    override fun onFailure(message: String?) {
                        if(System.currentTimeMillis() - startingTime >= 30000) {
                            myCamera.clearImageAnalyser()
                            finish()
                            EnvironmentOps.fireGCAfterFaceRegistration()
                        }
                    }

                }, assets)
            }

        },
            previewView,
            MyCamera.FRONT_CAMERA, ContextCompat.getMainExecutor(this))
        startingTime = System.currentTimeMillis()
    }
}