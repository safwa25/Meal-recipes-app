package com.example.recipeapp.home.home_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.MealAPI
import com.example.recipeapp.dto.MealArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(val repository: Repository): ViewModel() {
    private val _randomMeal = MutableLiveData<MealAPI?>()
    val randomMeal :LiveData<MealAPI?> get() = _randomMeal

    private val _randomMealList = MutableLiveData<List<MealAPI>?>()
    val randomMealList : MutableLiveData<List<MealAPI>?> get() = _randomMealList


    private val _areas = MutableLiveData<List<MealArea>>()
    val areas :LiveData<List<MealArea>> get() = _areas


    // Function to get random meal

    fun getRandomMeal(){
            viewModelScope.launch(Dispatchers.IO) {
                val randomMealList=repository.getRandomMeal()
                if (randomMealList.isSuccessful) {
                    val randomMealitem=randomMealList.body()?.meals?.get(0)
                    _randomMeal.postValue(randomMealitem)
                } else {
                }

            }
        }


    // Function to get random meal list
    fun getRandomMealList(){
        viewModelScope.launch(Dispatchers.IO) {
            val alphabet = "abcdefghijklmnopqrstuvwxyz"
            val rand = alphabet[Random.nextInt(alphabet.length)]
            val randomMealList=repository.searchMealByName(rand.toString())
            if (randomMealList.isSuccessful) {
                val randomMealitem=randomMealList.body()?.meals
                Log.d("Meal", "Success: ${randomMealitem?.size}")
                _randomMealList.postValue(randomMealitem)
            } else {
                Log.d("favMeal","Error in response")
            }

        }
    }


    // Function to get areas
    fun getAreas(){
        viewModelScope.launch(Dispatchers.IO) {
            val areas=repository.listAreas()
            if (areas.isSuccessful) {
                val areasList=areas.body()
                _areas.postValue(areasList?.meals)
            } else {
                Log.d("favMeal","Error in response")
            }

        }
    }

}