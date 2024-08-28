package com.example.recipeapp.authenticate.loginfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repo:Repository):ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun loadUserByEmailAndPassword(userEmail: String,passwordEmail:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue( repo.selectByEmailAndPassword(userEmail,passwordEmail))
        }
    }
}