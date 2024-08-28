package com.example.recipeapp.home.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogoutViewModel(private val repository: Repository ) :ViewModel()
{
    private val _currentuser = MutableLiveData<User>()
    val currentuser : LiveData<User> get() = _currentuser

  fun getUserById(userid:Int)
 {
      viewModelScope.launch(Dispatchers.IO) {
          _currentuser.postValue(repository.selectById(userid))
      }
 }
}