package com.example.recipeapp.home.favorite_fargment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.home.area_fragment.AreaViewModel
import java.lang.IllegalArgumentException

class FavoriteViewModelFactory (private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(repository) as T
        } else {
            throw IllegalArgumentException("areas view model class not found")
        }
    }
}