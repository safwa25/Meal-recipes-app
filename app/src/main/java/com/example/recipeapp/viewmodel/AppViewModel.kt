package com.example.recipeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AppViewModel(private val repo: Repository): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    fun loadUserById(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue( repo.selectById(userId))
        }
    }
    fun loadUserByEmail(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repo.selectByEmail(userEmail)
            if(user != null)
            {
                _userExists.postValue(true)
            }
            else
            {
                _userExists.postValue(false)
            }
        }
    }

    fun loadUserByEmailAndPassword(userEmail: String,passwordEmail:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue( repo.selectByEmailAndPassword(userEmail,passwordEmail))
        }
    }

    fun insertNewUser(user : User)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(user)
        }
    }
}