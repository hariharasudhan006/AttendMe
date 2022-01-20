package com.android.attendme.dataClasses

import java.io.Serializable


data class NewRegistrationUser(val email : String,
                               val password : String,
                               val institution : String
)

class TempNewUser : Serializable {
    var email : String = ""
    var password : String = ""
    var institution : String = ""
    fun copyTo() : NewRegistrationUser{
        return NewRegistrationUser(email, password, institution)
    }
    fun copy(user : NewRegistrationUser){
        email = user.email
        password = user.password
        institution = user.institution

    }
}
