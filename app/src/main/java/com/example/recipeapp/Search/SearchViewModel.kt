package com.example.recipeapp.Search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>?>()
    val categoryList: LiveData<List<Category>?> get() = _categoryList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getALLCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.listAllCategories()
                if (response.isSuccessful) {

                    _categoryList.postValue(response.body()?.categories)
                    Log.d("try enter",response.body()?.categories.toString())
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
}
