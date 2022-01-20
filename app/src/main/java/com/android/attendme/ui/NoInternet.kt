package com.android.attendme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.android.attendme.R

class NoInternet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        val lottieView = findViewById<LottieAnimationView>(R.id.no_internet_lottie_anime)
        lottieView.playAnimation()
    }
}