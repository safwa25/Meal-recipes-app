package com.example.recipeapp.home.home_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.viewmodel.AppViewModel
import java.lang.IllegalArgumentException

class HomeViewFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            HomeViewModel(repository) as T
        }
        else {
            throw IllegalArgumentException("home view model class not found")
        }
    }

}