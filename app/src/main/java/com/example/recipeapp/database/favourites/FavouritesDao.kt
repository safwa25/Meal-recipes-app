package com.example.recipeapp.database.favourites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.dto.Meal


@Dao
interface FavouritesDao {
    @Insert
    suspend fun insertFavourite(favourites: Favourites)

    @Query("SELECT m.* FROM favourites f, meal m WHERE m.idMeal = f.mealId and f.userId = :userId")
    suspend fun getFavourites(userId: Int): List<Meal>

    @Query("DELETE FROM favourites WHERE mealId = :mealId And userId = :userId")
    suspend fun deleteFavouriteByMealId(mealId: String, userId: Int)
}