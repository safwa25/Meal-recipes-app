package com.example.recipeapp.home.area_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.AreaMeal
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
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



    fun getMealByID(id:String): Meal?{
        var meal : Meal? = null
        viewModelScope.launch {
            val response = repo.lookupMealById(id)
            if(response.isSuccessful){
                meal = response.body()?.meals?.get(0)
            }
        }
        return meal
    }
}