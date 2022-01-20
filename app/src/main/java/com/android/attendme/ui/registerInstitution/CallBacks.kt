package com.android.attendme.ui.registerInstitution

interface CallBacks {
    fun nextBtInstitutionNameGetter()
    fun nextBtAdminNameGetter(email : String, password : String)
    fun contBtReTypePassword()
    fun nextBtInstitutionLocationGetter()
    fun backFromInstitutionGetter()
    fun backFromAdminGetter()
    fun backFromAdminReTypePass()
    fun backFromInstitutionLocationGetter()
}