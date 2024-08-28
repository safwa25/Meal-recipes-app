package com.example.recipeapp.home.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.home.home_fragment.HomeViewModel
import java.lang.IllegalArgumentException

class LogoutViewFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LogoutViewModel::class.java)) {
            LogoutViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Logout view model class not found")
        }
    }
}