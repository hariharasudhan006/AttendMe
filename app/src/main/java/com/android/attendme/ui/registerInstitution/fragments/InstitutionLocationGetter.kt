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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.android.attendme.R
import com.android.attendme.ui.registerInstitution.CallBacks

class InstitutionLocationGetter(callBacks : CallBacks, context : Context) : Fragment() {
    private var myCallBacks: CallBacks = callBacks
    private val con = context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.institution_location_getter, container, false)
        val backBtn = view.findViewById<TextView>(R.id.back_bt_institution_location)
        val useCurrentLocation = view.findViewById<CardView>(R.id.institution_location_use_current_location)
        val locationFromMap = view.findViewById<CardView>(R.id.institution_location_use_location_from_map)
        val anim = AnimationUtils.loadAnimation(con, R.anim.fade_in)
        useCurrentLocation.startAnimation(anim)
        locationFromMap.startAnimation(anim)
        Handler(Looper.getMainLooper()).postDelayed({
            backBtn.setOnClickListener {
                myCallBacks.backFromInstitutionLocationGetter()
            }
        }, 501)
        return view
    }
}