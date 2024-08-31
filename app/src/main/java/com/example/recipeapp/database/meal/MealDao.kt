package com.example.recipeapp.database.meal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.dto.Meal


@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(mealDataBase: Meal)


    @Query("SELECT * FROM meal where strMeal Like :mealName")
    suspend fun searchMeal(mealName :String) : List<Meal>


    @Query("Delete from meal Where idMeal = :mealId")
    suspend fun deleteMeal(mealId :String)
}