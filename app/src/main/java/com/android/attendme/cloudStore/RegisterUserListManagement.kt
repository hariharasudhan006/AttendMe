package com.android.attendme.cloudStore

import android.util.Log
import com.android.attendme.dataClasses.NewRegistrationUser
import com.android.attendme.dataClasses.TempNewUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RegisterUserListManagement {

    interface OnNewUserAddComplete{
        fun onSuccess()
        fun onFailure(message : String)
    }

    interface OnNewUserRemoveComplete{
        fun onSuccess()
        fun onFailure(message : String)
    }

    interface OnNewUserCheckCompleted{
        fun onUserExists(user : NewRegistrationUser)
        fun onUserNotExists()
        fun onErrorOccurs(message : String)
    }
    private val TAG = "RegisterListManagement"
    private val PATH = "register_user_list"
    private val executor : ExecutorService = Executors.newSingleThreadExecutor()

    fun putNewUser(user : NewRegistrationUser, listener : OnNewUserAddComplete){
        val tUser = TempNewUser()
        tUser.copy(user)
        executor.execute {
            try{
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection(PATH).document(user.email)
                docRef.set(tUser).addOnCompleteListener {
                    if(it.isSuccessful)
                        listener.onSuccess()
                    else{
                        listener.onFailure("Unable to register new user")
                        Log.d(TAG, "Unable to register new user", it.exception)
                    }
                }
            }catch (e:Exception){
                listener.onFailure("Unable to register new user")
                Log.d(TAG, "Unable to register new user", e)
            }
        }
    }

    fun removeNewUser(user: NewRegistrationUser, listener: OnNewUserRemoveComplete){
        executor.execute {
            try{
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection(PATH).document(user.email)
                docRef.delete().addOnCompleteListener {
                    if(it.isSuccessful)
                        listener.onSuccess()
                    else{
                        listener.onFailure("Unable to remove new user")
                        Log.d(TAG, "Unable to remove new user", it.exception)
                    }
                }
            }catch (e:Exception){
                listener.onFailure("Unable to remove new user")
                Log.d(TAG, "Unable to remove new user", e)
            }
        }
    }

    fun isNewUserPresent(user : NewRegistrationUser, listener : OnNewUserCheckCompleted){
        executor.execute {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection(PATH).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val snapShot = it.result
                        for(docSnapShot : DocumentSnapshot in snapShot.documents){
                            Log.i(TAG, docSnapShot.getField<String>("email") +" "+user.email)
                            if(docSnapShot.getField<String>("email") == user.email){
                               val u  : TempNewUser = docSnapShot.toObject()!!
                                listener.onUserExists(u.copyTo())
                                return@addOnCompleteListener
                            }
                        }
                        listener.onUserNotExists()
                    }else{
                        listener.onErrorOccurs("Unable to fetch user list document")
                        Log.d(TAG, "Unable to fetch user list document", it.exception)
                    }
                }
            }catch (e : Exception){
                listener.onErrorOccurs("Unable to fetch user list document")
                Log.d(TAG, "Unable to fetch user list document", e)
            }
        }
    }
}