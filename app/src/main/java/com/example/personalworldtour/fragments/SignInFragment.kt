package com.example.personalworldtour.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.personalworldtour.FragmentCommunicator
import com.example.personalworldtour.MainFrameActivity
import com.example.personalworldtour.R
import com.example.personalworldtour.databinding.SignInFragmentBinding
import com.example.personalworldtour.sql_lite.DatabaseHandler
import com.example.personalworldtour.sql_lite.UserData


class SignInFragment : Fragment(R.layout.sign_in_fragment) {

    private lateinit var bind : SignInFragmentBinding

    private lateinit var communicator : FragmentCommunicator


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        bind = SignInFragmentBinding.inflate(inflater,container,false)


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as FragmentCommunicator
        bind.btnToRegistration.setOnClickListener {
            communicator.toSignUpFragment()
        }

        bind.btnSignIn.setOnClickListener {
            val uName = bind.etUsername.text.toString()
            val pass = bind.etPassword.text.toString()
            val databaseHandler : DatabaseHandler = DatabaseHandler(requireContext())
            if(uName.isNotEmpty() && pass.isNotEmpty()){
                val uId = databaseHandler.findUserID(UserData(0,"","","",uName,pass))
                if(uId > -1){
                    communicator.signIn(uId)
                }else{
                    Toast.makeText(requireContext(),"Wrong username or password",Toast.LENGTH_LONG).show()
                }

            }


        }


    }


}