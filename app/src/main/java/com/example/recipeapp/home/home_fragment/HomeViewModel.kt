package com.example.recipeapp.home.home_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.example.recipeapp.database.user.User
import com.google.gson.Gson

class HomeViewModel(val repository: Repository, private val sharedPreferences: SharedPreferences): ViewModel() {
    private val _randomMeal = MutableLiveData<Meal?>()
    val randomMeal: LiveData<Meal?> get() = _randomMeal

    private val _randomMealList = MutableLiveData<List<Meal>?>()
    val randomMealList: LiveData<List<Meal>?> get() = _randomMealList

    private val _areas = MutableLiveData<List<MealArea>>()
    val areas: LiveData<List<MealArea>> get() = _areas

    private val _favorites = MutableLiveData<List<Meal>>()
    val favorites: LiveData<List<Meal>> get() = _favorites

    private val _currentUser = MutableLiveData<User>()
    val currentUser : LiveData<User> get() = _currentUser

    fun getRandomMeal() {
        if (_randomMeal.value == null) {
            val savedMeal = sharedPreferences.getString("random_meal_key", null)
            if (savedMeal != null) {
                _randomMeal.value = Gson().fromJson(savedMeal, Meal::class.java)
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val randomMealResponse = repository.getRandomMeal()
                    if (randomMealResponse.isSuccessful) {
                        val randomMealItem = randomMealResponse.body()?.meals?.get(0)
                        _randomMeal.postValue(randomMealItem)
                        sharedPreferences.edit().putString("random_meal_key", Gson().toJson(randomMealItem)).apply()
                    } else {
                        Log.d("HomeViewModel", "Failed to fetch random meal")
                    }
                }
            }
        }
    }

    fun getRandomMealList() {
        if (_randomMealList.value == null) {
            val savedMeals = sharedPreferences.getString("random_meal_list_key", null)
            if (savedMeals != null) {
                _randomMealList.value = Gson().fromJson(savedMeals, Array<Meal>::class.java).toList()
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val alphabet = "abcdefghijklmnopqrstuvwxyz"
                    val rand = alphabet[Random.nextInt(alphabet.length)]
                    val randomMealResponse = repository.searchMealByName(rand.toString())
                    if (randomMealResponse.isSuccessful) {
                        val randomMealItems = randomMealResponse.body()?.meals
                        _randomMealList.postValue(randomMealItems)
                        sharedPreferences.edit().putString("random_meal_list_key", Gson().toJson(randomMealItems)).apply()
                    } else {
                        Log.d("HomeViewModel", "Failed to fetch random meal list")
                    }
                }
            }
        }
    }

    fun getAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            val areasResponse = repository.listAreas()
            if (areasResponse.isSuccessful) {
                val areasList = areasResponse.body()
                _areas.postValue(areasList?.meals)
            } else {
                Log.d("HomeViewModel", "Failed to fetch areas")
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



    fun getUserById(userid:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(repository.selectById(userid))
        }
    }
}
