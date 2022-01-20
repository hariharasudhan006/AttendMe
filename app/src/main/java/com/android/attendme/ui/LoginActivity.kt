package com.android.attendme.ui

import android.content.Intent
import android.os.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.android.attendme.Hardware.MyVibrator
import com.android.attendme.Helpers.Helpers
import com.android.attendme.R
import com.android.attendme.auth.SignIn
import com.android.attendme.ui.dialogBoxes.PleaseWaitDialog
import com.android.attendme.ui.registerInstitution.RegisterInstitution
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.admin_name_password_getter.*

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEd : TextInputEditText
    private lateinit var passEd : TextInputEditText
    private lateinit var contBtn : AppCompatButton
    private lateinit var forgotPasswordBtn : TextView
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val regInstitution = findViewById<AppCompatButton>(R.id.reg_institution)
        val vibes = MyVibrator(applicationContext)
        emailEd = findViewById(R.id.login_email_tiet)
        passEd = findViewById(R.id.login_password_tiet)
        contBtn = findViewById(R.id.cont_btn_login)
        forgotPasswordBtn = findViewById(R.id.forgot_password_btn)
        contBtn.setOnClickListener {
            val email = emailEd.text.toString().trim()
            val password = passEd.text.toString().trim()
            if(Helpers.emailValidator(email)){
                if(Helpers.passwordValidator(password)){
                    val pleaseWaitDialog = PleaseWaitDialog()
                    val dialog = pleaseWaitDialog.showDialog(this)
                    SignIn().signIn(email, password,object : SignIn.OnSignInComplete{
                        override fun onUserNotExists() {
                            emailEd.error = "No account associated with this email"
                            dialog.dismiss()
                        }

                        override fun onSuccess() {
                            runOnUiThread{
                                Helpers.makeToast(applicationContext, "Signed in")
                            }
                            val auth = Firebase.auth
                            auth.signOut()
                            dialog.dismiss()
                        }

                        override fun onError(message: String) {
                            if(message.contains("Incorrect password", ignoreCase = true)){
                                passEd.error = "Incorrect password"
                            }
                            else{
                                runOnUiThread {
                                    Helpers.makeToast(applicationContext, "unable to sign in")
                                }
                            }
                            dialog.dismiss()
                        }
                    })
                }else{
                    passEd.error = "Password must have at least 8 characters"
                }
            }else{
                emailEd.error = "Invalid email"
            }
        }
        forgotPasswordBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }
        regInstitution.setOnClickListener {
            vibes.buttonClickVibration()
            val intent = Intent(this, RegisterInstitution::class.java)
            startActivity(intent)
        }
    }
}