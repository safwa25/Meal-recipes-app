package com.example.recipeapp.home.logout

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
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.authenticate.AuthenticateActivity
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient


class LogoutFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
   private lateinit var logoutButton : Button
   private lateinit var name_text:TextView
   private lateinit var email_text:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = LogoutViewFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()), FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )
        val viewModel = ViewModelProvider(this, viewModelFactory).get(LogoutViewModel::class.java)
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        logoutButton=view.findViewById(R.id.btn_logout)
        name_text=view.findViewById(R.id.username_text)
        email_text=view.findViewById(R.id.useremail_text)
        val userid:Int=sharedPreferences.getInt("id",-1)
Log.d("tktk", userid.toString())
        viewModel.getUserById(userid)
        viewModel.currentuser.observe(viewLifecycleOwner)
        {user->
            name_text.setText("Name : ${user.name}")
            email_text.setText("Email : ${user.email}")
        }
        logoutButton.setOnClickListener {
            val editor=sharedPreferences.edit()
            editor.putInt("id",-1)
            editor.apply()
            val intent = Intent(requireContext(), AuthenticateActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}