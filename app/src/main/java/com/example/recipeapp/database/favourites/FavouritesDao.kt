package com.example.recipeapp.database.favourites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.dto.Meal


@Dao
interface FavouritesDao {
    @Insert
    suspend fun insertFavourite(favourites: Favourites)

    @Query("SELECT m.* FROM favourites f, meal m WHERE m.idMeal = f.mealId and f.userId = :userId")
    suspend fun getFavourites(userId: Int): List<Meal>
}