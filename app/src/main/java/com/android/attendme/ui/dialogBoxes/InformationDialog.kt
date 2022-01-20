package com.android.attendme.ui.dialogBoxes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView
import com.android.attendme.R

class InformationDialog {
    private lateinit var dialog: Dialog
    @SuppressLint("UseCompatLoadingForDrawables")
    fun showDialog(activity : Activity, message : String){
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.prompt_dialog)
        dialog.window!!.setBackgroundDrawable(activity.getDrawable(R.drawable.please_wait_dialog_background))
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        val lottieView = dialog.findViewById<LottieAnimationView>(R.id.lottie_info_view_prompt_dialog)
        lottieView.visibility = View.VISIBLE
        val dismissBtn = dialog.findViewById<AppCompatButton>(R.id.dismiss_btn_prompt_dialog)
        val textView = dialog.findViewById<TextView>(R.id.information_text_view_prompt_dialog)
        textView.text = String.format("%s", message)
        dialog.show()
        dismissBtn.setOnClickListener {
            dialog.dismiss()
        }
    }
}