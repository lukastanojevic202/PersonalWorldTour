package com.example.personalworldtour.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personalworldtour.Constants
import com.example.personalworldtour.FragmentCommunicator
import com.example.personalworldtour.R
import com.example.personalworldtour.sql_lite.UserData
import com.example.personalworldtour.databinding.SignUpFragmentBinding
import com.example.personalworldtour.sql_lite.DatabaseHandler


class SignupFragment: Fragment(R.layout.sign_up_fragment) {
    private lateinit var bind : SignUpFragmentBinding

//    private var displayMessage : String? = null
    lateinit var communicator : FragmentCommunicator

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bind = SignUpFragmentBinding.inflate(inflater,container,false)
//        displayMessage = arguments?.getString("message")
        return bind.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communicator = activity as FragmentCommunicator
        bind.btnCreateAcc.setOnClickListener {
            accountCreateAttempt()
        }

    }
    fun checkInput(name : String, last : String,email : String, user : String, pass : String) : Int{
        if(name.isEmpty() || !name.contains(Regex("^[A-Z][a-z]{1,48}$"))){
            return Constants.FIRS_NAME_ERROR
        }
        if(last.isEmpty() || !last.contains(Regex("^[A-Z][a-z]{1,48}$"))){
            return Constants.LAST_NAME_ERROR
        }
        if(email.isEmpty() || !email.contains(Regex("^[a-zA-Z0-9.]+[@][a-z]+\\.[a-z]{3}\$"))){
            return Constants.EMAIL_ERROR
        }
        if(user.isEmpty() || !user.contains(Regex("^[a-zA-Z0-9_]{1,49}$"))){
            return Constants.USERNAME_ERROR
        }
        if(!(pass.contains(Regex("^[a-zA-Z0-9_.!]{8,49}$"))
                        && pass.contains(Regex("[1-9]+"))
                        && pass.contains(Regex("[_!.]+")))){
            return Constants.PASSWORD_ERROR
        }
        return Constants.VALID_INPUT
    }
    fun accountCreateAttempt(){
        val fName : String = bind.etFirstName.text.toString()
        val lName : String = bind.etLastName.text.toString()
        val email : String = bind.etEmail.text.toString()
        val uName : String = bind.etSetUsername.text.toString()
        val pass : String = bind.etSetPassword.text.toString()

        val databaseHandler : DatabaseHandler = DatabaseHandler(requireContext())
// Check if al fields are inputted correctly
        when(checkInput(fName,lName,email,uName,pass)){
            Constants.FIRS_NAME_ERROR ->
                Toast.makeText(activity,"First name starts with capital letter",
                        Toast.LENGTH_LONG).show()
            Constants.LAST_NAME_ERROR ->
                Toast.makeText(activity,"Last name starts with capital letter",
                        Toast.LENGTH_LONG).show()
            Constants.EMAIL_ERROR ->
                Toast.makeText(activity,"Incorrect E-mail address",
                        Toast.LENGTH_LONG).show()
            Constants.USERNAME_ERROR ->
                Toast.makeText(activity,"Username can contain regular characters, digits and special characters _ or !",
                        Toast.LENGTH_LONG).show()
            Constants.PASSWORD_ERROR ->
                Toast.makeText(activity,"Password must contain at least one digit and one special character _ . or ! ",
                        Toast.LENGTH_LONG).show()
            Constants.VALID_INPUT ->{
                val checkStatus = databaseHandler.findUser(UserData(0,fName,lName,email,uName,pass))
                if(checkStatus){
                    val status = databaseHandler.addUser(UserData(0,fName,lName,email,uName,pass))
                    if(status > -1){
                        Toast.makeText(requireContext(),"Registration complete successfully",Toast.LENGTH_LONG).show()
                        clearEts()
                        communicator.toSignInFragment()
                    }
                }else {
                    Toast.makeText(requireContext(),"Acc already exists",Toast.LENGTH_LONG).show()
                    communicator.toSignInFragment()
                }

            }
            else -> Toast.makeText(activity,"Error in app",
                    Toast.LENGTH_LONG).show()
        }
    }
    fun clearEts(){
        bind.etFirstName.text!!.clear()
        bind.etLastName.text!!.clear()
        bind.etEmail.text!!.clear()
        bind.etSetUsername.text!!.clear()
        bind.etSetPassword.text!!.clear()
    }




}