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

class first_splash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_splash, container, false)

        // Initialize your ImageView
        val next: ImageView = view.findViewById(R.id.next)
        val skip:TextView=view.findViewById(R.id.skip)
        next.setOnClickListener {
            findNavController().navigate(R.id.action_first_splash_to_second_splash)
        }
        skip.setOnClickListener {
            findNavController().navigate(R.id.action_first_splash_to_loginFragment6)
        }

        return view
    }
}
