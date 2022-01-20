package com.android.attendme.auth

import android.util.Log
import com.android.attendme.cloudStore.UserListManagement
import com.android.attendme.cloudStore.UserListManagement.OnUserCheckComplete
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateNewAccount {
        private val TAG = "CreateNewAccount"
        interface OnCreateNewComplete{
            fun onSuccess()
            fun onFailure(message : String)
        }
        fun createNewAccount(email : String,
                         password : String,
                         listener : OnCreateNewComplete
        ){
            val auth = Firebase.auth
            val userManagement = UserListManagement()
            userManagement.isUserAlreadyPresent(email, object : OnUserCheckComplete{
                override fun onUserExists() {
                    listener.onFailure("User already exists")
                }
                override fun onUserNotExists() {
                    try{
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if(it.isSuccessful) {
                                    listener.onSuccess()
                                    UserListManagement().addUserToList(email,
                                        object : UserListManagement.OnUserAddComplete{
                                            override fun onSuccess() {
                                                Log.i(TAG, "User was added to list")
                                            }
                                            override fun onFailure(message: String) {
                                                Log.d(TAG, message);
                                            }
                                        })
                                }
                                else{
                                    listener.onFailure("Unable to create user")
                                    Log.d(TAG, "Unable to create new user", it.exception)
                                }

                            }
                    }catch (e : Exception){
                        listener.onFailure("Unable to create user")
                        Log.d(TAG, "Unable to create new user", e)
                    }
                }
                override fun onErrorOccurs(message: String) {
                    listener.onFailure(message)
                }

            })
        }
}