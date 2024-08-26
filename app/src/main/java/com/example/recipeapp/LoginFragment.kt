package com.example.recipeapp

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.LocalDataBaseImplement
import com.example.recipeapp.viewmodel.AppViewModel
import com.example.recipeapp.viewmodel.AppViewModelFactory

class LoginFragment : Fragment() {
    private lateinit var username : TextView
    private lateinit var password : TextView
    private lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.loginBtn)
        val signupTextView = view?.findViewById<TextView>(R.id.signup_text)
        val viewModelFactory = AppViewModelFactory(RepositoryImplement(LocalDataBaseImplement(this.requireContext())))
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AppViewModel::class.java)


        loginButton.setOnClickListener {
            viewModel.loadUserByEmailAndPassword(username.text.toString(), password.text.toString())
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("asd", "User: ${it.email}")
            } else {
                Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }


        val spannableString = SpannableString("Don't Have One? Sign Up")
        val signUpClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // handle with a NavGraph
            }
        }
    }
}


