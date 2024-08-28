package com.example.recipeapp.home.home_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repository: Repository): ViewModel() {
    private val _randomMeal = MutableLiveData<MealAPI?>()
    val randomMeal :LiveData<MealAPI?> get() = _randomMeal

    fun getRandomMeal(){
            viewModelScope.launch(Dispatchers.IO) {
                val randomMealList=repository.getRandomMeal()
                if (randomMealList.isSuccessful) {
                    val randomMealitem=randomMealList.body()?.meals?.get(0)
                    Log.d("Meal", "Success: ${randomMealitem?.idMeal.toString()}")
                    _randomMeal.postValue(randomMealitem)
                } else {
                  Log.d("favMeal","Error in response")
                }

            }
        }

}