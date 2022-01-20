package com.android.attendme.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun isSignedIn():Boolean{
    val user = Firebase.auth.currentUser
    return user != null
}