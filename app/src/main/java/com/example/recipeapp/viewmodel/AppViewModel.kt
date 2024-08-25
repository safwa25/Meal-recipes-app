package com.example.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(private val repo: Repository): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun loadUserById(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue( repo.selectById(userId))
        }
    }
    fun loadUserByEmail(userEmail: String,passwordEmail:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue( repo.selectByEmail(userEmail,passwordEmail))
        }
    }
    fun insertNewUser(user : User)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(user)
        }
    }
}