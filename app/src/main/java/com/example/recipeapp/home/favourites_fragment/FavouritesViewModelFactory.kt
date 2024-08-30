package com.example.recipeapp.home.favourites_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.home.area_fragment.AreaViewModel
import java.lang.IllegalArgumentException

class FavouritesViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavouritesViewModel::class.java))
        {
            FavouritesViewModel(repository) as T
        }
        else {
            throw IllegalArgumentException("favourites view model class not found")
        }
    }
}