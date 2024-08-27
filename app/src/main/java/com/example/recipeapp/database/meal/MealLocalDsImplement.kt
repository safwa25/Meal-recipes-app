package com.example.recipeapp.database.meal

import android.content.Context
import com.example.recipeapp.database.AppDatabase
import com.example.recipeapp.dto.MealDataBase

class MealLocalDsImplement(context: Context) : MealLocalDs {
    private val mealDao: MealDao
    init {
        val dp= AppDatabase.getDatabase(context)
        mealDao =dp.mealDao()
    }
    override suspend fun insertMeal(mealDataBase: MealDataBase) {
        mealDao.insertMeal(mealDataBase)
    }

    override suspend fun searchMeals(mealName: String): List<MealDataBase> {
        return mealDao.searchMeal(mealName)
    }
}