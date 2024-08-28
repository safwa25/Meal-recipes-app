package com.example.recipeapp.authenticate.signupfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.authenticate.loginfragment.LoginViewModel
import java.lang.IllegalArgumentException

class SignupViewModelFactory( val repo: Repository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            SignupViewModel(repo) as T
        } else {
            throw IllegalArgumentException("Signup view model class not found")
        }
    }
}