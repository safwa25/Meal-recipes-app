package com.example.recipeapp.authenticate.loginfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import java.lang.IllegalArgumentException

class LoginViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(LoginViewModel::class.java))
        {
            LoginViewModel(repo) as T
        }
        else {
            throw IllegalArgumentException("Login view model class not found")
        }
    }
    }