package com.example.recipeapp.home.favorite_fargment

import SpaceItemDecoration
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient


class Favorite : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = FavoriteViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()), FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )
        val viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        val userid:Int=sharedPreferences.getInt("id",-1)
        Log.d("tktk", userid.toString())
        viewModel.getFavoriteMeals(userid)
        val recyclerView = view.findViewById<RecyclerView>(R.id.favorite_recyclerview)
        val favouritestext=view.findViewById<TextView>(R.id.favoritesEmpty_text)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(30))
        recyclerView.layoutManager = layoutManager
        viewModel.favoriteMealsList.observe(viewLifecycleOwner)
        {
            rendomMeal-> Log.d("tktk","List  = ${rendomMeal}")
        }
        viewModel.favoriteMealsList.observe(viewLifecycleOwner) { Meals ->
            if(!Meals.isEmpty()) {
                recyclerView.isInvisible=false
                favouritestext.isInvisible=true
                adapter = FavoriteAdapter(Meals){ meal, fab ->
                    viewModel.deleteFavoriteMeals(meal.idMeal,userid)
                    fab.setImageResource(R.drawable.baseline_favorite_border_24)
                    Toast.makeText(context, "removed to favourites", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = adapter
            }
            else
            {
                favouritestext.isInvisible=false
                recyclerView.isInvisible=true
            }
        }

    }



}