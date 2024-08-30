package com.example.recipeapp.authenticate.loginfragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.home.HomeActivity
import com.example.recipeapp.network.APIClient

class LoginFragment : Fragment() {
    private lateinit var username : TextView
    private lateinit var password : TextView
    private lateinit var loginButton : Button
    private lateinit var sharedPreferences: SharedPreferences

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
        val signupTextView = view.findViewById<TextView>(R.id.signup_text)
        val loginviewModelFactory = LoginViewModelFactory(
            RepositoryImplement(
            LocalDataBaseImplement(this.requireContext()),
            MealLocalDsImplement(this.requireContext()), FavouritesLocalDsImplement(this.requireContext()),APIClient))
        val viewModel = ViewModelProvider(this, loginviewModelFactory).get(LoginViewModel::class.java)
        loginButton.setOnClickListener {
            viewModel.loadUserByEmailAndPassword(username.text.toString(), password.text.toString())
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("asd", "User: ${it.email}")
                sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
                val editor=sharedPreferences.edit()
                editor.putInt("id",it.id)
                editor.apply()
                val homepageIntent = Intent(requireContext(), HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(homepageIntent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }
        signupTextView.setOnClickListener{
            findNavController().navigate(R.id.signup_fragment)
        }
    }
}


