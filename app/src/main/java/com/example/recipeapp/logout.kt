package com.example.recipeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.recipeapp.authenticate.AuthenticateActivity


class logout : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
   private lateinit var logoutButton : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var view=inflater.inflate(R.layout.fragment_logout, container, false)
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        logoutButton=view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            val editor=sharedPreferences.edit()
            editor.putInt("id",-1)
            editor.apply()
            val intent = Intent(requireContext(), AuthenticateActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return view
    }
}