package com.example.recipeapp.SplashFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.recipeapp.home.HomeActivity
import com.example.recipeapp.R

class SplashFragmentThree : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_splash_three, container, false)
        val next: ImageView = view.findViewById(R.id.next)
        next.setOnClickListener()
        {
            val homepage= Intent(view.context, HomeActivity::class.java)
            startActivity(homepage)
            requireActivity().finish()
        }


        return view
    }


}