package com.example.personalworldtour

import com.google.android.material.textfield.TextInputEditText

interface FragmentCommunicator {
    fun toSignUpFragment()
    fun toSignInFragment()
    fun signIn(uId : Int)
}