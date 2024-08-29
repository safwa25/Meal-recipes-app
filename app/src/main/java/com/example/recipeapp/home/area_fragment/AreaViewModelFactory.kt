package com.example.recipeapp.home.area_fragment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import java.lang.IllegalArgumentException

class AreaViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AreaViewModel::class.java))
        {
            AreaViewModel(repository) as T
        }
        else {
            throw IllegalArgumentException("areas view model class not found")
        }
    }

}