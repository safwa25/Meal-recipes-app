package com.example.recipeapp.home.area_fragment

import SpaceItemDecoration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.home.home_fragment.HomeViewFactory
import com.example.recipeapp.home.home_fragment.HomeViewModel
import com.example.recipeapp.network.APIClient
import com.example.task2.AreasMealsAdapter

class AreaFragment : Fragment() {
    private lateinit var adapter: AreasMealsAdapter

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

        val areaName = arguments?.getString("areaName") ?: ""
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AreaViewModel::class.java)

        adapter = AreasMealsAdapter(viewModel.areasMeals.value ?: emptyList())

        val recyclerView = view.findViewById<RecyclerView>(R.id.areas_meals_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(20))

        viewModel.areasMeals.observe(viewLifecycleOwner) { areasMeals ->
            adapter = AreasMealsAdapter(viewModel.areasMeals.value ?: emptyList())
            recyclerView.adapter = adapter
        }

        viewModel.getAreasMeals(areaName)


    }


}