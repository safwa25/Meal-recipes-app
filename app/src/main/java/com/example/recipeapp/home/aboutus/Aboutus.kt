package com.example.recipeapp.home.aboutus

import SpaceItemDecoration
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.home.favorite_fargment.FavoriteAdapter


class Aboutus : Fragment() {
    private lateinit var adapter: AboutUsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aboutus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
        toolbar.title ="About Us"
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_personalCards)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(30))
        recyclerView.layoutManager = layoutManager
        val names = arrayOf("Foodiii","Safwa", "Abdelrahman","Habiba","Ahmed")
        val emails = arrayOf("Foodiii.contactus@gmail.com","safwaibrahim8@gmail.com","abdelrahmantamer0111@gmail.com","habiba@gmail.com","ahmed@gmail.com")
        val images=arrayOf(R.mipmap.foodii,R.drawable.safwa,R.drawable.abdelrahman,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_foreground)
        adapter = AboutUsAdapter(names, emails, images) { email ->
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                Log.d("EmailIntent", "email = ${email}")
            }
            try {
                startActivity(emailIntent)
            }catch (e:Exception)
            {
                Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
                Log.d("asd","email : ${e.message}")
            }

        }
        recyclerView.adapter = adapter
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.fragment_home)
            }
        })
    }

}