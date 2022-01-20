package com.android.attendme.ui.registerInstitution.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.attendme.ui.registerInstitution.CallBacks
import com.android.attendme.Helpers.Helpers
import com.android.attendme.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AdminNameGetterFragment(callBacks: CallBacks, context: Context) : Fragment() {
    private val myCallbacks = callBacks
    private val con = context
    private lateinit var myView : View
    private lateinit var emailEd : TextInputEditText
    private lateinit var passEd : TextInputEditText

    private fun validate(email : String, password : String) : Boolean{
        if(!Helpers.emailValidator(email)){
            emailEd.error = "Invalid email"
            return false
        }
        if(!Helpers.passwordValidator(password)){
            passEd.error = "Password must have at least 8 character"
            return false
        }
        return true
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(
            R.layout.admin_name_password_getter, container,
            false)
        val userNameLayout =
            myView.findViewById<TextInputLayout>(R.id.username_layout_in_admin_name_getter)
        val passwordLayout =
            myView.findViewById<TextInputLayout>(R.id.password_layout_in_admin_name_getter)
        val contBt = myView.findViewById<AppCompatButton>(R.id.next_bt_admin_name_getter)
        emailEd = myView.findViewById(R.id.email_admin_registration)
        passEd = myView.findViewById(R.id.password_admin_registration)
        val anim = AnimationUtils.loadAnimation(con, R.anim.slide)
        userNameLayout.startAnimation(anim)
        passwordLayout.startAnimation(anim)
        val backBt = myView.findViewById<TextView>(R.id.back_bt_admin)
        Handler(Looper.getMainLooper()).postDelayed({
            backBt.setOnClickListener { myCallbacks.backFromAdminGetter() }
            contBt.setOnClickListener {
                val email = emailEd.text.toString().trim()
                val password = passEd.text.toString().trim()
                if(validate(email, password)){
                    myCallbacks.nextBtAdminNameGetter(
                        email,
                        password
                    )
                }
            }
        },501)
        return myView
    }
}