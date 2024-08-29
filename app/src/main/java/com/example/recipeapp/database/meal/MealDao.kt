package com.example.recipeapp.database.meal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.dto.Meal


@Dao
interface MealDao {

    @Insert
    suspend fun insertMeal(mealDataBase: Meal)


    @Query("SELECT * FROM meal where strMeal Like :mealName")
    suspend fun searchMeal(mealName :String) : List<Meal>
}