package com.example.recipeapp.database.favourites

import com.example.recipeapp.dto.Meal

class FavouritesLocalDsImplement(private val favouritesDao: FavouritesDao) : FavouritesLocalDs {
    override suspend fun insertFavourite(favourite: Favourites) {
        favouritesDao.insertFavourite(favourite)
    }

    override suspend fun getFavourites(userId: Int): List<Meal> {
        return favouritesDao.getFavourites(userId)
    }
}