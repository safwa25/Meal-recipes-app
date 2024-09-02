package com.example.recipeapp.home.recipe_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.appstorage.Repository
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.dto.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(val repo:Repository):ViewModel() {
    private val _userFavorites =MutableLiveData<Boolean>()
    val userFavorites:LiveData<Boolean> get() = _userFavorites

    fun getUserFavourites(userId:Int,mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
           _userFavorites.postValue(repo.checkMeal(mealId,userId)!=null)
        }

    }
    fun insertFavourite(meal: Meal,userId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertMeal(meal)
                repo.insertFavourite(Favourites(meal.idMeal.toString(),userId))
                getUserFavourites(userId,meal.idMeal.toString())
            }
            catch (e:Exception){
                Log.d("safwa","error in inserting user favourites")
            }

        }

    }
    fun deleteUserFavourite(mealId:String,userId: Int){
        viewModelScope.launch(Dispatchers.IO){
            try{
                repo.deleteFavouriteByMealId(mealId, userId)
                getUserFavourites(userId,mealId)
            }catch (e:Exception){
                Log.d("safwa","error in deleting user favourite item")
            }
        }
    }

}