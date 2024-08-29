package com.example.recipeapp.database.meal

import com.example.recipeapp.dto.Meal

interface MealLocalDs {
    suspend fun insertMeal(mealDataBase: Meal)
    suspend fun searchMeals(mealName : String): List<Meal>
    suspend fun deleteMeal(mealId :String)
}