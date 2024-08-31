package com.example.recipeapp.SplashFragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.home.HomeActivity
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
            findNavController().navigate(R.id.second_splash)
        }
        skip.setOnClickListener {
            val homepage=Intent(view.context, HomeActivity::class.java)
            startActivity(homepage)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes") { _, _ ->
                        requireActivity().finishAffinity() // Exit the app
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })


        return view
    }
}
