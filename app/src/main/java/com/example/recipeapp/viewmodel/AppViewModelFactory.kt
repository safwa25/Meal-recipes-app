package com.example.recipeapp.viewmodel

import com.example.recipeapp.appstorage.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AppViewModelFactory (private val repo: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AppViewModel::class.java))
        {
            AppViewModel(repo) as T
        }
        else {
            throw IllegalArgumentException("App view model class not found")
        }
    }
}


