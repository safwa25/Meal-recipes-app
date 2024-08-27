package com.example.recipeapp.database.meal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.dto.MealDataBase


@Dao
interface MealDao {

    @Insert
    suspend fun insertMeal(mealDataBase: MealDataBase)


    @Query("SELECT * FROM meal where strMeal Like :mealName")
    suspend fun searchMeal(mealName :String) : List<MealDataBase>
}