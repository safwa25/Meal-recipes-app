package com.example.recipeapp.database.favourites

import com.example.recipeapp.dto.MealDataBase

interface FavouritesLocalDs {
    suspend fun insertFavourite(favourite: Favourites)
    suspend fun getFavourites(userId: Int): List<MealDataBase>
}