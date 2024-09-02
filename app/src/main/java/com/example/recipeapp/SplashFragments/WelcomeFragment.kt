package com.example.recipeapp.SplashFragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.home.HomeActivity
import com.example.recipeapp.R

class WelcomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)

        // Find the TextViews
        val logoTextView = view.findViewById<TextView>(R.id.textView)
        val subtitleTextView = view.findViewById<TextView>(R.id.textView3)

        // Log for debugging
        Log.d("WelcomeFragment", "TextViews found: $logoTextView, $subtitleTextView")

        // Set initial state for animations
        logoTextView.alpha = 0f
        logoTextView.translationY = 100f
        subtitleTextView.alpha = 0f
        subtitleTextView.translationY = 100f

        // Ensure animations run after the view is laid out
        view.post {
            Log.d("WelcomeFragment", "Starting animations")

            // Create animations for logo TextView
            val fadeInLogo = ObjectAnimator.ofFloat(logoTextView, "alpha", 0f, 1f).apply {
                duration = 1500
            }
            val slideUpLogo = ObjectAnimator.ofFloat(logoTextView, "translationY", 100f, 0f).apply {
                duration = 1500
            }

            // Create animations for subtitle TextView
            val fadeInSubtitle = ObjectAnimator.ofFloat(subtitleTextView, "alpha", 0f, 1f).apply {
                duration = 1500
                startDelay = 500 // Delay to start after logo animation
            }
            val slideUpSubtitle = ObjectAnimator.ofFloat(subtitleTextView, "translationY", 100f, 0f).apply {
                duration = 1500
                startDelay = 500
            }

            // Combine animations into AnimatorSet
            val animatorSet = AnimatorSet().apply {
                interpolator = AccelerateDecelerateInterpolator()
                playTogether(fadeInLogo, slideUpLogo, fadeInSubtitle, slideUpSubtitle)
            }

            // Start animations
            animatorSet.start()
        }

        // Handler to navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val id = sharedPreferences.getInt("id", -1)
            Log.d("WelcomeFragment", "SharedPreferences id: $id")
            if (id == -1 || id == 0) {
                findNavController().navigate(R.id.loginFragment)
            } else {
                val homepage = Intent(requireContext(), HomeActivity::class.java)
                startActivity(homepage)
                requireActivity().finish()
            }
        }, 3000)

        // Handle back press
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
