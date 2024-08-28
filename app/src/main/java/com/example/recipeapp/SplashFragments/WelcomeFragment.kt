package com.example.recipeapp.SplashFragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.home.HomeActivity
import com.example.recipeapp.R


class WelcomeFragment : Fragment() {
lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val startButton = view.findViewById<Button>(R.id.start_btn)
         sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        startButton.setOnClickListener {

            val id =sharedPreferences.getInt("id",-1)
            if(id==-1 || id==0)
            {
                findNavController().navigate(R.id.loginFragment)
            }else
            {
                val homepage= Intent(view.context, HomeActivity::class.java)
                startActivity(homepage)
            }

        }
        return view

    }

}