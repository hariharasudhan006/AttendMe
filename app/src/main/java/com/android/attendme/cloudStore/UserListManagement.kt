package com.android.attendme.cloudStore

import android.util.Log
import com.android.attendme.Helpers.Helpers
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserListManagement {

    interface OnUserCheckComplete{
        fun onUserExists()
        fun onUserNotExists()
        fun onErrorOccurs(message : String)
    }
    interface OnUserAddComplete{
        fun onSuccess()
        fun onFailure(message : String)
    }
    companion object{
        private const val TAG = "UserListManagement"
        const val PATH = "user_list"
    }
    private val executor : ExecutorService = Executors.newSingleThreadExecutor()


    fun isUserAlreadyPresent(email : String, listener : OnUserCheckComplete){
        val myEmail = Helpers.removeSpecialCharacters(email)
        executor.execute {
            try {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection(PATH).document(myEmail)
                docRef.get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val snapShot = it.result
                        if(snapShot.exists()){
                            listener.onUserExists()
                        }else{
                            listener.onUserNotExists()
                        }
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

    fun addUserToList(email: String, listener : OnUserAddComplete){
        val myEmail = Helpers.removeSpecialCharacters(email)
        executor.execute {
            try{
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection(PATH).document(myEmail)
                val map = HashMap<String, String>()
                map["email"] = email
                docRef.set(map).addOnCompleteListener {
                    if(it.isSuccessful){
                        listener.onSuccess()
                    }else{
                        listener.onFailure("Unable to add user")
                        Log.d(TAG, "Unable to add user", it.exception)
                    }
                }
            }catch (e : Exception){
                listener.onFailure("Unable to add user")
                Log.d(TAG, "Unable to add user", e)
            }
        }
    }
}