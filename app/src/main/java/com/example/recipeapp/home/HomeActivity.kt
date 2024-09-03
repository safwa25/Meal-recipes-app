package com.example.recipeapp.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.recipeapp.databinding.HomeActivityBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivityBinding
    private lateinit var fabButton :FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fabButton=findViewById(R.id.fab)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.tool_bar)
        toolbar.title ="Welcome,User"
        setSupportActionBar(toolbar)



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        // Set the default selected item to the middle item (home)
        bottomNavigationView.selectedItemId = R.id.home

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                AlertDialog.Builder(this@HomeActivity)
//                    .setTitle("Exit")
//                    .setMessage("Are you sure you want to exit?")
//                    .setPositiveButton("Yes") { _, _ ->
//                        finish()
//                    }
//                    .setNegativeButton("No", null)
//                    .show()
//            }
//        })

        fabButton.setOnClickListener {
            findNavController(R.id.fragment_host).navigate(R.id.fragment_home)

        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
        when(item.itemId){
            R.id.favourite->{
                findNavController(R.id.fragment_host).navigate(R.id.favorite)
                true
            }
            R.id.search->
            {
                findNavController(R.id.fragment_host).navigate(R.id.searchFragment)
                Log.d("habiba","navigate to search")
                true
            }
            else -> false
        }
        }
        visibility(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_item -> {
                findNavController(R.id.fragment_host).navigate(R.id.fragment_logout)
                true
            }
            R.id.option_settings -> {
               findNavController(R.id.fragment_host).navigate(R.id.settings_fragment)
                true
            }
            R.id.favourite->{
                findNavController(R.id.fragment_host).navigate(R.id.fragment_home)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    fun visibility(flag:Boolean){
        val appbar=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        appbar.isInvisible=flag
        val btnHome: FloatingActionButton =findViewById(R.id.fab)
        btnHome.isInvisible=flag
    }


}