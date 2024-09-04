package com.example.recipeapp.home.area_fragment

import SpaceItemDecoration
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.home.home_fragment.HomeFragmentDirections
import com.example.recipeapp.network.APIClient
import com.example.task2.AreasMealsAdapter

class AreaFragment : Fragment() {
    private lateinit var adapter: AreasMealsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loadingAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the loading animation view
        loadingAnimationView = view.findViewById(R.id.loading_animation)

        val viewModelFactory = AreaViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()),
                FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )

        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)

        val areaName = arguments?.getString("areaName") ?: ""
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
        toolbar.title = "$areaName Meals"

        val viewModel = ViewModelProvider(this, viewModelFactory).get(AreaViewModel::class.java)

        adapter = AreasMealsAdapter(viewModel.areasMeals.value ?: emptyList(), { meal ->
            if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
                viewModel.deleteFavourite(meal, userId)
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavourite(meal, userId)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
        }, { meal ->
            val action = AreaFragmentDirections.actionAreaFragmentToRecipeDetails(meal)
            findNavController().navigate(action)
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.areas_meals_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(20))

        viewModel.areasMeals.observe(viewLifecycleOwner) { meals ->
            val favoriteIds = viewModel.favorites.value?.map { it.idMeal }?.toSet() ?: emptySet()
            adapter.updateData(meals ?: emptyList(), favoriteIds)

            // Hide the loading animation when data is loaded
            loadingAnimationView.cancelAnimation() // Stop the animation
            loadingAnimationView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites?.map { it.idMeal }?.toSet() ?: emptySet()
            adapter.updateData(viewModel.areasMeals.value ?: emptyList(), favoriteIds)
        }

        // Show the loading animation before starting data fetch
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation() // Start the animation
        recyclerView.visibility = View.GONE

        viewModel.getAreasMeals(areaName)
        viewModel.getFavorites(userId)
    }
}
