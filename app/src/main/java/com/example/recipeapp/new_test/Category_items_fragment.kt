package com.example.recipeapp.new_test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.R
import com.example.recipeapp.Search.SearchViewModel
import com.example.recipeapp.Search.SearchViewModelFactory
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient


class Category_items_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_items_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//         Observe LiveData from ViewModel
        val viewModelFactory = SearchViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(requireContext()),
                MealLocalDsImplement(requireContext()),
                FavouritesLocalDsImplement(requireContext()),
                APIClient
            )
        )
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        viewModel.getMealsbyCategory("SeaFood")
        viewModel.Categorttype.observe(viewLifecycleOwner) { categorttype ->
            Log.d("CategoryFragment", "Observer triggered with ${categorttype?.size} items")
            categorttype?.let {
//                categoryitemsAdaptor.updateData(it)
                Log.d("CategoryFragment", "Adapter data updated with ${it.size} items")
            }
        }

    }


}