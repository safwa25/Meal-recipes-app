package com.example.recipeapp.appstorage

import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.Meal

interface Repository {
    suspend fun insert(user: User)
    suspend fun selectById(userId :Int) : User?
    suspend fun selectByEmail(userEmail :String) : User?
    suspend fun selectByEmailAndPassword(userEmail :String,passwordEmail:String) : User?
    suspend fun insertMeal(meal: Meal)
    suspend fun searchMeals(mealName : String): List<Meal>
    suspend fun insertFavourite(favourite: Favourites)
    suspend fun getFavourites(userId: Int): List<Meal>

}