package com.example.recipeapp.home.home_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.dto.MealDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repository: Repository): ViewModel() {
    private val _randomMealDataBase = MutableLiveData<MealDataBase>()
    val randomMealDataBase :LiveData<MealDataBase> get() = _randomMealDataBase

    private val _isRandomError= MutableLiveData<String>()
    val isRandomError:LiveData<String> =_isRandomError

    fun getRandomMeal(){
            viewModelScope.launch {
                try {
                    // Perform network request on IO dispatcher
                    val response = repository.getRandomMeal()

                    // Ensure response is successful
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _randomMealDataBase.postValue(it)
                            Log.d("asd",it.toString())
                        } ?: run {
                            _isRandomError.postValue("Empty response body")
                            Log.d("asd","Empty response")

                        }
                    } else {
                        _isRandomError.postValue("Error: ${response.code()} ${response.message()}")
                        Log.d("asd","Error")

                    }
                } catch (e: Exception) {
                    // Handle any exceptions during network call
                    _isRandomError.postValue("Exception: ${e.localizedMessage}")
                    Log.d("asd","Clear")

                }
            }
        }

}