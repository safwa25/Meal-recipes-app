package com.example.recipeapp.home.favorite_fargment

import SpaceItemDecoration
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.dto.Meal
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
        viewModel.getFavoriteMeals(userid)
        val recyclerView = view.findViewById<RecyclerView>(R.id.favorite_recyclerview)
        val favouritestext=view.findViewById<TextView>(R.id.favoritesEmpty_text)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(30))
        recyclerView.layoutManager = layoutManager

        adapter = FavoriteAdapter(emptyList(),{ meal, fab ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Remove Favorite")
            builder.setMessage("Are you sure you want to remove ${meal.strMeal} meal from your favorites?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteFavoriteMeals(meal.idMeal, userid)
                fab.setImageResource(R.drawable.baseline_favorite_border_24)
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                viewModel.getFavoriteMeals(userid)
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        },isNetworkAvailable(), { clickedMeal -> onRecipeClick(clickedMeal) })
        recyclerView.adapter = adapter

        viewModel.favoriteMealsList.observe(viewLifecycleOwner) { Meals ->
            if(!Meals.isEmpty()) {
                recyclerView.isInvisible=false
                favouritestext.isInvisible=true
                adapter.meals = Meals
                adapter.notifyDataSetChanged()
            }
            else
            {
                favouritestext.isInvisible=false
                recyclerView.isInvisible=true
            }
        }


    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }
    fun onRecipeClick(meal: Meal) {
        val action = FavoriteDirections.actionFavoriteToRecipeDetails(meal)
        findNavController().navigate(action)
    }




}