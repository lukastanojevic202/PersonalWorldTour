package com.example.personalworldtour

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.personalworldtour.fragments.SignInFragment
import com.example.personalworldtour.fragments.SignupFragment
import com.google.android.material.textfield.TextInputEditText


class StartActivity : AppCompatActivity(), FragmentCommunicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, SignInFragment()).commit()
    }

    override fun toSignUpFragment() {
        val bundle = Bundle()
        val transaction = this.supportFragmentManager.beginTransaction()
        val signupFragment = SignupFragment()
        transaction.replace(R.id.fragmentContainer,signupFragment).addToBackStack(null).commit()
    }

    override fun toSignInFragment() {
        val transaction = this.supportFragmentManager.beginTransaction()
        val signinFragment = SignInFragment()

        transaction.replace(R.id.fragmentContainer,signinFragment).commit()
    }

    override fun signIn(uId : Int) {
        val intent = Intent(this, MainFrameActivity::class.java)
        intent.putExtra("userId",uId)
//        saveData(uId)

        startActivity(intent)
        finish()
    }


//    private fun saveData(uid : Int) {
//        val id = uid
//        val sharedPreferences : SharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        editor.apply{
//            putInt("INT_KEY",id)
//        }.apply()
//    }
//    private fun loadData(){
//        val sharedPreferences : SharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
//        val savedInt = sharedPreferences.getInt("INT_KEY",-1)
//    }



}