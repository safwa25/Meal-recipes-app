package com.example.recipeapp.database.favourites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.dto.Meal


@Dao
interface FavouritesDao {
    @Insert
    suspend fun insertFavourite(favourites: Favourites)

    @Query("SELECT * FROM favourites f , user u, meal m where f.userId = u.id and f.mealId = m.idMeal and u.id = :userId")
    suspend fun getFavourites(userId :Int) : List<Meal>

}