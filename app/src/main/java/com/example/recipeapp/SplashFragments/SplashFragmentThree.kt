package com.example.recipeapp.SplashFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(R.id.action_splashFragmentThree_to_loginFragment6)
        }


        return view
    }


}