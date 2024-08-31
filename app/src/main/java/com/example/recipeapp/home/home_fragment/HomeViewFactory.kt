package com.example.recipeapp.home.home_fragment

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import java.lang.IllegalArgumentException

class HomeViewFactory(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository, sharedPreferences) as T
        } else {
            throw IllegalArgumentException("HomeViewModel class not found")
        }
    }
}