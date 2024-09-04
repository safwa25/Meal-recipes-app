package com.example.recipeapp.home.recipe_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import java.lang.IllegalArgumentException

class DetailsViewModelFactory(private val repo: Repository) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            DetailsViewModel(repo) as T
        }
            else {
                throw IllegalArgumentException("DetailsViewModel is not found")
            }
        }
    }