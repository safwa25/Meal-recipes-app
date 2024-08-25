package com.example.recipeapp

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LoginFragment : Fragment() {

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
        val signupTextView = view?.findViewById<TextView>(R.id.signup_text)

        val spannableString = SpannableString("Don't Have One? Sign Up")
        val signUpClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // handle with a NavGraph
            }
        }

//        // Set the color for "Sign Up"
//        spannableString.setSpan(
//            ForegroundColorSpan(Color.BLUE),
//            17, // Start index of "Sign Up"
//            24, // End index of "Sign Up"
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        // Make "Sign Up" clickable
//        spannableString.setSpan(
//            signUpClickableSpan,
//            17, // Start index of "Sign Up"
//            24, // End index of "Sign Up"
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        signupTextView?.text = spannableString
//        signupTextView?.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}


