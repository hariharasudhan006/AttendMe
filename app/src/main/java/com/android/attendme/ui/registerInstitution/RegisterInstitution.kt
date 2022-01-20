package com.android.attendme.ui.registerInstitution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.attendme.Helpers.Helpers
import com.android.attendme.ui.dialogBoxes.PleaseWaitDialog
import com.android.attendme.R
import com.android.attendme.auth.CreateNewAccount
import com.android.attendme.ui.registerInstitution.fragments.*

class RegisterInstitution : AppCompatActivity() {
    private var defaultFragment = Fragment()
    private var institutionNameGetterFragment = Fragment()
    private var adminReEnterPass = Fragment()
    private lateinit var institutionLocationGetter : Fragment
    private lateinit var callbacks : CallBacks
    companion object{
        private lateinit var emailId : String
        lateinit var pass : String
    }
    private fun setDefaultFragment(){
        setFragment(defaultFragment)
    }
    fun setFragment(fragment : Fragment){
        val fragmentManager = this.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_institution)
        callbacks = object : CallBacks {
            override fun nextBtInstitutionNameGetter() {
                setFragment(institutionLocationGetter)
            }
            override fun nextBtAdminNameGetter(email: String, password: String) {
                emailId = email
                pass = password
                setFragment(adminReEnterPass)
            }
            override fun contBtReTypePassword(){
                val pleaseWaitDialog = PleaseWaitDialog()
                val dialog = pleaseWaitDialog.showDialog(this@RegisterInstitution)
                CreateNewAccount().createNewAccount(emailId, pass,
                     object : CreateNewAccount.OnCreateNewComplete{
                         override fun onSuccess() {
                             runOnUiThread {
                                 setFragment(institutionNameGetterFragment)
                                 Helpers.makeToast(applicationContext, "Success")
                                 dialog.dismiss()
                             }
                         }
                         override fun onFailure(message: String) {
                             runOnUiThread {
                                 Helpers.makeToast(applicationContext, "Unable to create new account")
                                 finish()
                                 dialog.dismiss()
                             }
                         }

                     }
                 )
            }

            override fun nextBtInstitutionLocationGetter() {
                finish()
            }

            override fun backFromInstitutionGetter(){finish()}
            override fun backFromAdminGetter(){ finish() }
            override fun backFromAdminReTypePass(){
                setFragment(defaultFragment)
            }
            override fun backFromInstitutionLocationGetter() {
                setFragment(institutionNameGetterFragment)
            }
        }
        institutionNameGetterFragment = InstitutionNameGetterFragment(callbacks, applicationContext)
        defaultFragment = AdminNameGetterFragment(callbacks, applicationContext)
        adminReEnterPass = AdminReEnterPasswordFragment(callbacks, applicationContext)
        institutionLocationGetter = InstitutionLocationGetter(callbacks, applicationContext)
        setDefaultFragment()
    }
}