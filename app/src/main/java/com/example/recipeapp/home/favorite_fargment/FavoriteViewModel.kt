package com.example.recipeapp.home.favorite_fargment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(val repo : Repository) : ViewModel() {

    private val _favoriteMealsList = MutableLiveData<List<Meal>>()
    val favoriteMealsList : LiveData<List<Meal>> get() = _favoriteMealsList

    fun getFavoriteMeals(userid : Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteMealsList.postValue(repo.getFavourites(userid))
        }

    }

    fun deleteFavoriteMeals(mealid:String,userid: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavouriteByMealId(mealid,userid)
        }
    }


}