package com.android.attendme.auth

import android.util.Log
import com.android.attendme.cloudStore.RegisterUserListManagement
import com.android.attendme.dataClasses.NewRegistrationUser
import java.util.concurrent.Executors

class RegisterNewAccount {

    interface OnRegisterNewAccountComplete{
        fun onSuccess()
        fun onFailure(message : String)
    }
    private val TAG = "RegisterNewAccount"
    fun registerNewAccount(email : String,
                           password : String,
                           listener : OnRegisterNewAccountComplete
    ){
        val user = NewRegistrationUser(email, password, "Nothing")
        RegisterUserListManagement().putNewUser(user, object : RegisterUserListManagement.OnNewUserAddComplete{
            override fun onSuccess() {
                listener.onSuccess()
            }
            override fun onFailure(message: String) {
                listener.onFailure(message)
                Log.d(TAG, message)
            }

        })
    }
}