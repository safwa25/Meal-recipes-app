package com.example.recipeapp.home.area_fragment

import SpaceItemDecoration
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = AreaViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()), FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)

        val areaName = arguments?.getString("areaName") ?: ""
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
        toolbar.title ="$areaName Meals"

        val viewModel = ViewModelProvider(this, viewModelFactory).get(AreaViewModel::class.java)

        adapter = AreasMealsAdapter(viewModel.areasMeals.value ?: emptyList(),{meal ->
        if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
            viewModel.deleteFavourite(meal, userId)
        } else {
            viewModel.insertFavourite(meal, userId)
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
            adapter?.updateData(meals ?: emptyList(), favoriteIds)
        }

        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites?.map { it.idMeal }?.toSet() ?: emptySet()
            adapter?.updateData(viewModel.areasMeals.value ?: emptyList(), favoriteIds)
        }

        viewModel.getAreasMeals(areaName)
        viewModel.getFavorites(userId)


    }


}