package com.android.attendme.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.android.attendme.R

class DashBoard : AppCompatActivity() {
    private lateinit var myAttendanceLottieAnime : LottieAnimationView
    private lateinit var myAccountLottieAnime : LottieAnimationView
    private lateinit var myInstitutionAnime : LottieAnimationView
    private lateinit var updateFaceDataAnime : LottieAnimationView
    private lateinit var animator : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        myAttendanceLottieAnime = findViewById(R.id.myAttendanceLottieAnime)
        myAccountLottieAnime = findViewById(R.id.myAccountLottieAnime)
        myInstitutionAnime = findViewById(R.id.myInstitutionAnime)
        updateFaceDataAnime = findViewById(R.id.updateFaceDataAnime)
        playAnimations()
        animator.start()
     }
    private fun playAnimations(){
        animator = object : CountDownTimer(1200000, 20000){
            override fun onTick(p0: Long) {
                myAttendanceLottieAnime.playAnimation()
                myAccountLottieAnime.playAnimation()
                myInstitutionAnime.playAnimation()
                updateFaceDataAnime.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    updateFaceDataAnime.progress = 0F
                }, 150)
            }
            override fun onFinish() {
                myAttendanceLottieAnime.playAnimation()
                myAccountLottieAnime.playAnimation()
                myInstitutionAnime.playAnimation()
                updateFaceDataAnime.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    updateFaceDataAnime.progress = 0F
                }, 150)
            }
        }
    }
}