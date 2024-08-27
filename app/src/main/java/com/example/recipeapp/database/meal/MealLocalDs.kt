package com.example.recipeapp.database.meal

import com.example.recipeapp.dto.MealDataBase

interface MealLocalDs {
    suspend fun insertMeal(mealDataBase: MealDataBase)
    suspend fun searchMeals(mealName : String): List<MealDataBase>
}