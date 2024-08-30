package com.example.recipeapp.home.favourites_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesViewModel(val repo : Repository) : ViewModel() {
    private val _favourites = MutableLiveData<List<Meal>>()
    val favourites : LiveData<List<Meal>> get() = _favourites


    fun getFavourites(userId: Int){
        viewModelScope.launch {
            _favourites.postValue(repo.getFavourites(userId))
        }
    }

    fun deleteFavourite(meal: Meal, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteMeal(meal.idMeal)
                repo.deleteFavouriteByMealId(meal.idMeal, userId)
                getFavourites(userId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Delete favourite failed", e)
            }
        }
    }
}