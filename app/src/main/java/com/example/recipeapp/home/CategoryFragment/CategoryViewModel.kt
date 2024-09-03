package com.example.recipeapp.home.CategoryFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.CategoryFilterClass
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: Repository) : ViewModel() {
    private val _Categorttype = MutableLiveData<List<CategoryFilterClass>>()
    val Categorttype: LiveData<List<CategoryFilterClass>> = _Categorttype


    fun getMealsbyCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = repository.filterByCategory(category)
                if (response.isSuccessful) {
                    _Categorttype.postValue(response.body()?.meals)
                    Log.d(
                        "SearchViewModel",
                        "Network request completed, success: ${response.isSuccessful}"
                    )
                } else {
                    Log.e(
                        "SearchViewModel",
                        "Failed to fetch meals by category: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception during network request: ${e.message}")
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
}


