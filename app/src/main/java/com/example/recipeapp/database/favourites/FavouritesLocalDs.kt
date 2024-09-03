package com.example.recipeapp.database.favourites

import com.example.recipeapp.dto.Meal

interface FavouritesLocalDs {
    suspend fun insertFavourite(favourite: Favourites)

    suspend fun getFavourites(userId: Int): List<Meal>

    suspend fun deleteFavouriteByMealId(mealId: String,userId: Int)

    suspend fun checkMeal(mealId: String,userId: Int) : String?
}