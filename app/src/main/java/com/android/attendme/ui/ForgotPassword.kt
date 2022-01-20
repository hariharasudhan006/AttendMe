package com.android.attendme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.android.attendme.R
import com.android.attendme.ui.dialogBoxes.PleaseWaitDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors


class ForgotPassword : AppCompatActivity() {


    private fun validateEmail(email : String) : Boolean{
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+(\\.+[a-z]+)?"
        return email.matches(emailPattern.toRegex())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val emailEd = findViewById<TextInputEditText>(R.id.forgot_email_tiet)
        val submitBtn = findViewById<AppCompatButton>(R.id.forgot_password_submit_btn)
        submitBtn.setOnClickListener {
            val email = emailEd.text.toString().trim()
            if(validateEmail(email)){
                val pleaseWaitDialog = PleaseWaitDialog()
                val dialog = pleaseWaitDialog.showDialog(this)

                Executors.newSingleThreadExecutor().execute {
                    val auth = Firebase.auth
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener {
                            if(it.isSuccessful) {
                                runOnUiThread{
                                    Toast.makeText(applicationContext,
                                        "email sent",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    dialog.dismiss()
                                }
                            }
                            else {
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Something went wrong",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    dialog.dismiss()
                                }
                            }
                        }
                }
            }else
                emailEd.error = "Invalid email"
        }
    }

}