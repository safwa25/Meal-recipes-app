package com.example.recipeapp.database.meal

import com.example.recipeapp.dto.Meal

interface MealLocalDs {
    suspend fun insertMeal(meal: Meal)
    suspend fun searchMeals(mealName : String): List<Meal>
}