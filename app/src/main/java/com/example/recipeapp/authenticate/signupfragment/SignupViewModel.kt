package com.example.recipeapp.authenticate.signupfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(private val repo:Repository):ViewModel() {

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> get() = _userId


    fun loadUserByEmail(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repo.selectByEmail(userEmail)
            if (user != null) {
                _userExists.postValue(true)
            } else {
                _userExists.postValue(false)
            }
        }
    }

    fun insertUserAndFetchLastId(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(user)
            val lastId = repo.returnlastid()
            _userId.postValue(lastId)
        }
    }

}