package com.example.recipeapp.home.CategoryFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.dto.CategoryFilterClass
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: Repository) : ViewModel() {
    private val _Categorttype = MutableLiveData<List<Meal>?>()
    val Categorttype: LiveData<List<Meal>?> = _Categorttype

    private val _favorites = MutableLiveData<List<Meal>>()
    val favorites: LiveData<List<Meal>> get() = _favorites


    fun getMealsbyCategory (category: String) {
        viewModelScope.launch {
            val response = repository.filterByCategory(category)
            if (response.isSuccessful) {
                val areaMeals = response.body()?.meals
                val meals = areaMeals?.mapNotNull { areaMeal ->
                    val mealResponse = repository.lookupMealById(areaMeal.idMeal.toString())
                    if (mealResponse.isSuccessful) {
                        mealResponse.body()?.meals?.getOrNull(0) // Get the first meal or null
                    } else {
                        null
                    }
                }
                _Categorttype.postValue(meals)
            } else {
                _Categorttype.postValue(null)
            }
        }
    }

    private val _meal = MutableLiveData<Meal>()
    val meal: LiveData<Meal> = _meal

    fun getMealsbyname(name: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.searchMealByName(name)
                if (response.isSuccessful) {
                    _meal.postValue(response.body()?.meals?.get(0))
                    Log.d(
                        "habiba",
                        "Network request completed, success: ${response.isSuccessful}"
                    )
                } else {
                    Log.d(
                        "habiba",
                        "Failed to fetch meals by category: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("habiba", "Exception during network request: ${e.message}")
            }
        }

    }

    fun insertFavourite(meal: Meal, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertMeal(meal)
                repository.insertFavourite(Favourites(meal.idMeal.toString(), userId))
                getFavorites(userId) // Refresh favorites list after insertion
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Insert favourite failed", e)
            }
        }
    }

    fun deleteFavourite(meal: Meal, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouriteByMealId(meal.idMeal, userId)
                getFavorites(userId) // Refresh favorites list after deletion
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Delete favourite failed", e)
            }
        }
    }

    fun getFavorites(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoritesList = repository.getFavourites(userId)
            _favorites.postValue(favoritesList)
        }
    }
}


