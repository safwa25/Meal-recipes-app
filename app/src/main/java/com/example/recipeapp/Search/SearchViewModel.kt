package com.example.recipeapp.Search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.SearchClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>?>()
    val categoryList: LiveData<List<Category>?> get() = _categoryList

    private val _SearchList = MutableLiveData<List<SearchClass>?>()
    val SearchList: LiveData<List<SearchClass>?> get() = _SearchList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getALLCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.listAllCategories()
                if (response.isSuccessful) {

                    _categoryList.postValue(response.body()?.categories)
                    Log.d("SearchViewModel", "Response body: ${response.body()}")
                    Log.d("try enter", response.body()?.categories.toString())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("SearchViewModel", "Error: $errorBody")
                    _errorMessage.postValue("Failed to load categories: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception: ${e.message}", e)
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }

    fun getMealByName(mealName: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchMealByName(mealName)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    Log.d("SearchViewModel", "Search results: $meals")
                    _SearchList.value = meals
                } else {
                    val errorMsg = "Error: ${response.errorBody()?.string()}"
                    Log.e("SearchViewModel", errorMsg)
                    _errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception: ${e.message}", e)
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

}
