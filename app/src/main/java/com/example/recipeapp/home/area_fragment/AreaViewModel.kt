package com.example.recipeapp.home.area_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.dto.AreaMeal
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AreaViewModel(val repo : Repository) : ViewModel(){
    private val _areasMeals = MutableLiveData<List<Meal>?>()
    val areasMeals : MutableLiveData<List<Meal>?> get() = _areasMeals

    private val _favorites = MutableLiveData<List<Meal>>()
    val favorites: LiveData<List<Meal>> get() = _favorites


    fun getAreasMeals(area: String) {
        viewModelScope.launch {
            val response = repo.filterByArea(area)
            if (response.isSuccessful) {
                val areaMeals = response.body()?.meals
                val meals = areaMeals?.mapNotNull { areaMeal ->
                    val mealResponse = repo.lookupMealById(areaMeal.idMeal.toString())
                    if (mealResponse.isSuccessful) {
                        mealResponse.body()?.meals?.getOrNull(0) // Get the first meal or null
                    } else {
                        null
                    }
                }
                _areasMeals.postValue(meals)
            } else {
                _areasMeals.postValue(null)
            }
        }
    }




//    fun getMealByID(id: String): LiveData<Meal?> {
//        val mealLiveData = MutableLiveData<Meal?>()
//        viewModelScope.launch {
//            val response = repo.lookupMealById(id)
//            if (response.isSuccessful) {
//                mealLiveData.postValue(response.body()?.meals?.get(0))
//            } else {
//                mealLiveData.postValue(null)
//            }
//        }
//        return mealLiveData
//    }

    fun insertFavourite(meal: Meal, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertMeal(meal)
                repo.insertFavourite(Favourites(meal.idMeal.toString(), userId))
                getFavorites(userId) // Refresh favorites list after insertion
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Insert favourite failed", e)
            }
        }
    }

    fun deleteFavourite(meal: Meal, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteFavouriteByMealId(meal.idMeal, userId)
                getFavorites(userId) // Refresh favorites list after deletion
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Delete favourite failed", e)
            }
        }
    }

    fun getFavorites(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoritesList = repo.getFavourites(userId)
            _favorites.postValue(favoritesList)
        }
    }

}