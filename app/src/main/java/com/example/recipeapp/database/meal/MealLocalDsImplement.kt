package com.example.recipeapp.database.meal

import android.content.Context
import com.example.recipeapp.database.AppDatabase
import com.example.recipeapp.database.user.UserDao
import com.example.recipeapp.dto.Meal

class MealLocalDsImplement(context: Context) : MealLocalDs {
    private val mealDao: MealDao
    init {
        val dp= AppDatabase.getDatabase(context)
        mealDao =dp.mealDao()
    }
    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun searchMeals(mealName: String): List<Meal> {
        return mealDao.searchMeal(mealName)
    }
}