package com.example.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(private val repo: Repository): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists


    private val _favourites = MutableLiveData<List<Meal>>()
    val favourites: LiveData<List<Meal>> get() = _favourites


    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals




    // user -------------------------
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


    // meals -------------------------
    fun insertNewMeal(meal: Meal)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertMeal(meal)
        }
    }

    fun searchMeals(mealName: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _meals.postValue(repo.searchMeals(mealName))
        }
    }


    // favourite meals -------------------------
    fun getFavoriteMeals(userId: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _favourites.postValue(repo.getFavourites(userId))
        }
    }


    fun insertFavourite(favourite: Favourites)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertFavourite(favourite)
        }
    }
}