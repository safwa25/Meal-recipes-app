package com.example.recipeapp.SplashFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R



class second_splash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view2 = inflater.inflate(R.layout.fragment_second_splash, container, false)
        val next: ImageView = view2.findViewById(R.id.next)
        val skip:TextView=view2.findViewById(R.id.skip)
        next.setOnClickListener {
            findNavController().navigate(R.id.action_second_splash_to_splashFragmentThree)
        }
        skip.setOnClickListener()
        {
            findNavController().navigate(R.id.action_second_splash_to_loginFragment6)
        }

        return view2
    }

}