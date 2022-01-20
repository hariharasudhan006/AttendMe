package com.android.attendme.ui.registerInstitution.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.attendme.Hardware.MyVibrator
import com.android.attendme.R
import com.android.attendme.ui.registerInstitution.CallBacks

class InstitutionNameGetterFragment(callBacks : CallBacks, context: Context) : Fragment() {
    private val myCallbacks = callBacks
    private val con = context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.institution_name_getter, container, false)
        val nextBt = view.findViewById<AppCompatButton>(R.id.next_bt_institution_reg)
        val vibes = MyVibrator(con)
        nextBt.setOnClickListener { myCallbacks.nextBtInstitutionNameGetter(); vibes.buttonClickVibration() }
        val backBt = view.findViewById<TextView>(R.id.back_bt_institution)
        backBt.setOnClickListener { myCallbacks.backFromInstitutionGetter() }
        return view
    }
}