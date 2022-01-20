package com.android.attendme.auth

import android.util.Log
import com.android.attendme.cloudStore.RegisterUserListManagement
import com.android.attendme.cloudStore.UserListManagement
import com.android.attendme.dataClasses.NewRegistrationUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class SignIn {

    interface OnSignInComplete{
        fun onUserNotExists()
        fun onSuccess()
        fun onError(message : String)
    }

    interface OnCreateNewRegisterAccountComplete{
        fun onUserNotExists()
        fun onSuccess()
        fun onError(message : String)
    }

    private val executor = Executors.newSingleThreadExecutor()
    private val TAG = "SignIn"
    fun signIn(email : String,
               password : String,
               listener: OnSignInComplete
    ){
        executor.execute {
            try{
            UserListManagement().isUserAlreadyPresent(
                email,
                object : UserListManagement.OnUserCheckComplete{
                    override fun onUserExists() {
                        val auth = Firebase.auth
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    listener.onSuccess()
                                }
                                else{
                                    if(it.exception.toString().contains("The password is invalid",
                                            ignoreCase = true)){
                                        listener.onError("Incorrect password")
                                    }else{
                                        listener.onError("unable to login")
                                        Log.d(TAG, "Unable to login", it.exception)
                                    }
                                }
                            }
                    }
                    override fun onUserNotExists() {
                        createNewRegisteredAccount(email, password,
                            object : OnCreateNewRegisterAccountComplete{
                                override fun onUserNotExists() {
                                    listener.onUserNotExists()
                                }

                                override fun onSuccess() {
                                    listener.onSuccess()
                                }

                                override fun onError(message: String) {
                                    listener.onError(message)
                                }
                            }
                        )
                        return
                    }

                    override fun onErrorOccurs(message: String) {
                        listener.onError("unable to login")
                    }

                }
            )
            }catch (e : Exception){
                listener.onError("unable to login")
                Log.d(TAG, "Unable to login", e)
            }
        }
    }
    private fun createNewRegisteredAccount(email : String,
                                   password:String,
                                   listener : OnCreateNewRegisterAccountComplete
    ){
        try {
            val user = NewRegistrationUser(email, password, "nothing")
            RegisterUserListManagement().isNewUserPresent(user,
                object : RegisterUserListManagement.OnNewUserCheckCompleted {
                    override fun onUserExists(user: NewRegistrationUser) {
                        if (password == user.password) {
                            CreateNewAccount().createNewAccount(email, password,
                                object : CreateNewAccount.OnCreateNewComplete{
                                    override fun onSuccess() {
                                        RegisterUserListManagement().removeNewUser(user,
                                            object : RegisterUserListManagement.OnNewUserRemoveComplete{
                                                override fun onSuccess() {
                                                    listener.onSuccess()
                                                    Log.i(TAG, "User was removed from register list")
                                                }

                                                override fun onFailure(message: String) {
                                                    Log.d(TAG, message);
                                                    listener.onError(message)
                                                }

                                            })
                                    }
                                    override fun onFailure(message: String) {
                                        listener.onError(message)
                                    }
                                }
                            )
                        } else {
                            listener.onError("Incorrect password")
                        }
                    }

                    override fun onUserNotExists() {
                        listener.onUserNotExists()
                    }

                    override fun onErrorOccurs(message: String) {
                        listener.onError(message)
                    }

                }
            )
        }catch ( e : Exception){
            listener.onError("Unable to login")
            Log.d(TAG, "Unable to create new user", e)
        }
    }
}