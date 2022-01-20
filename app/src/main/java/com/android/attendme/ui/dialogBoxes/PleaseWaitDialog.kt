package com.android.attendme.ui.dialogBoxes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import com.android.attendme.R

class PleaseWaitDialog {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showDialog(activity: Activity) : Dialog{
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.please_wait_dialog)
        dialog.window!!.setBackgroundDrawable(activity.getDrawable(R.drawable.please_wait_dialog_background))
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}