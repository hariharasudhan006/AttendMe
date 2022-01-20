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
import com.android.attendme.R
import com.android.attendme.ui.registerInstitution.RegisterInstitution
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AdminReEnterPasswordFragment(callBacks: CallBacks, context: Context) : Fragment() {
    private val con = context
    private val myCallBacks = callBacks
    private lateinit var retypePassEd : TextInputEditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_reenter_password, container, false)
        retypePassEd = view.findViewById(R.id.pass_in_admin_re_type_pass)
        val contBt = view.findViewById<AppCompatButton>(R.id.cont_bt_admin_re_type_pass)
        val backBt = view.findViewById<TextView>(R.id.back_bt_admin_re_type_pass)
        val reTypePassLayout = view.findViewById<TextInputLayout>(R.id.pass_in_admin_re_type_pass_layout)
        val anim = AnimationUtils.loadAnimation(con, R.anim.slide)
        reTypePassLayout.startAnimation(anim)
        Handler(Looper.getMainLooper()).postDelayed({
            backBt.setOnClickListener { myCallBacks.backFromAdminReTypePass() }
            contBt.setOnClickListener {
                if(RegisterInstitution.pass == retypePassEd.text.toString().trim())
                    myCallBacks.contBtReTypePassword()
                else
                    retypePassEd.error = "Password doesn't match"

            }
        },501)
        return view
    }
}