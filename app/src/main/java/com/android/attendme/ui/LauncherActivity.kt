package com.android.attendme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.attendme.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_launcher)
        val intent = Intent(this@LauncherActivity, FaceRegistration::class.java)
            /*when {
            isSignedIn() -> {
                Intent(this@LauncherActivity, DashBoard::class.java)
            }
            !Helpers.isConnectedToInternet(applicationContext) -> {
                Intent(this@LauncherActivity, NoInternet::class.java)
            }
            else -> {
                Intent(this@LauncherActivity, LoginActivity::class.java)
            }
        }*/
        startActivity(intent)
        finish()
    }
}