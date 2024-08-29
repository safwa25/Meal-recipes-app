package com.example.recipeapp.database.favourites

import android.content.Context
import com.example.recipeapp.database.AppDatabase
import com.example.recipeapp.dto.Meal

class FavouritesLocalDsImplement(context: Context) : FavouritesLocalDs {
    private val favouritesDao: FavouritesDao
    init {
        val dp= AppDatabase.getDatabase(context)
        favouritesDao =dp.favoriteDao()
    }
    override suspend fun insertFavourite(favourite: Favourites) {
        favouritesDao.insertFavourite(favourite)
    }

    override suspend fun getFavourites(userId: Int): List<Meal> {
        return favouritesDao.getFavourites(userId)
    }

    override suspend fun deleteFavouriteByMealId(mealId: String) {
        favouritesDao.deleteFavouriteByMealId(mealId)
    }
}