package com.example.recipeapp.database.meal

import com.example.recipeapp.dto.Meal

class MealLocalDsImplement(private val mealDao: MealDao) : MealLocalDs {
    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun searchMeals(mealName: String): List<Meal> {
        return mealDao.searchMeal(mealName)
    }
}