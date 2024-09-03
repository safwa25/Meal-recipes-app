package com.example.recipeapp.home.Search


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.CategoryFilterClass
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>?>()
    val categoryList: LiveData<List<Category>?> get() = _categoryList

    private val _SearchList = MutableLiveData<List<Meal>?>()
    val SearchList: LiveData<List<Meal>?> get() = _SearchList


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getALLCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true) // Set loading to true before starting the request
            try {
                val response = repository.listAllCategories()
                if (response.isSuccessful) {
                    val categories = response.body()?.categories ?: emptyList()
                    Log.d("SearchViewModel", "Categories: $categories")
                    _categoryList.postValue(categories)
                    _errorMessage.postValue(null) // Clear any previous errors
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SearchViewModel", "Error: $errorBody")
                    _errorMessage.postValue("Failed to load categories: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception: ${e.message}", e)
                _errorMessage.postValue("An error occurred: ${e.message}")
            } finally {
                _isLoading.postValue(false) // Set loading to false when the request is complete
            }
        }
    }

    fun getMealByName(mealName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true) // Set loading to true before starting the request
            try {
                val response = repository.searchMealByName(mealName)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    Log.d("SearchViewModel", "Search results: $meals")
                    _SearchList.postValue(meals)
                    _errorMessage.postValue(null) // Clear any previous errors
                } else {
                    val errorMsg = "Error: ${response.errorBody()?.string()}"
                    Log.e("SearchViewModel", errorMsg)
                    _errorMessage.postValue(errorMsg)
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception: ${e.message}", e)
                _errorMessage.postValue("Exception: ${e.message}")
            } finally {
                _isLoading.postValue(false) // Set loading to false when the request is complete
            }
        }
    }

    private val _checking = MutableLiveData<Boolean>()
    val checking: LiveData<Boolean> get() = _checking

    fun checing(MealId:String,UserId:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
           val response = repository.checkMeal(MealId,UserId)
            if(response.isNullOrEmpty())
            {
                _checking.postValue(false)
            }
            else
            {
                _checking.postValue(true)
            }
        }

    }

}


