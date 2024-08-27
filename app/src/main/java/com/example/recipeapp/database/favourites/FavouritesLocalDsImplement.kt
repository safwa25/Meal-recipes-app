package com.example.recipeapp.database.favourites

import android.content.Context
import com.example.recipeapp.database.AppDatabase
import com.example.recipeapp.dto.MealDataBase

class FavouritesLocalDsImplement(context: Context) : FavouritesLocalDs {
    private val favouritesDao: FavouritesDao
    init {
        val dp= AppDatabase.getDatabase(context)
        favouritesDao =dp.favoriteDao()
    }
    override suspend fun insertFavourite(favourite: Favourites) {
        favouritesDao.insertFavourite(favourite)
    }

    override suspend fun getFavourites(userId: Int): List<MealDataBase> {
        return favouritesDao.getFavourites(userId)
    }
}