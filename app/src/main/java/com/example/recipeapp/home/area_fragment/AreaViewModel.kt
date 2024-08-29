package com.example.recipeapp.home.area_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.AreaMeal
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AreaViewModel(val repo : Repository) : ViewModel(){
    private val _areasMeals = MutableLiveData<List<AreaMeal>?>()
    val areasMeals : MutableLiveData<List<AreaMeal>?> get() = _areasMeals



    fun getAreasMeals(area:String){
        viewModelScope.launch {
            val response = repo.filterByArea(area)
            if(response.isSuccessful){
                _areasMeals.postValue(response.body()?.meals)
            }
        }
    }



    fun getMealByID(id: String): LiveData<Meal?> {
        val mealLiveData = MutableLiveData<Meal?>()
        viewModelScope.launch {
            val response = repo.lookupMealById(id)
            if (response.isSuccessful) {
                mealLiveData.postValue(response.body()?.meals?.get(0))
            } else {
                mealLiveData.postValue(null)
            }
        }
        return mealLiveData
    }

}