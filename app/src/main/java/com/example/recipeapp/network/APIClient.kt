package com.example.recipeapp.network

import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal

object APIClient:RemoteDataSource {
    override suspend fun searchMealByName(mealName: String): List<Meal> {
        return API.retrofitService.searchMealByName(mealName)
    }

    override suspend fun listMealsByFirstLetter(firstLetter: String): List<Meal> {
        return API.retrofitService.listMealsByFirstLetter(firstLetter)
    }

    override suspend fun lookupMealById(mealId: String): Meal {
       return API.retrofitService.lookupMealById(mealId)
    }

    override suspend fun getRandomMeal(): Meal {
        return API.retrofitService.getRandomMeal()
    }

    override suspend fun listAllCategories(): List<Category> {
        return API.retrofitService.listAllCategories()
    }

    override suspend fun listCategories(): List<String> {
        return API.retrofitService.listCategories()
    }

    override suspend fun listAreas(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun listIngredients(): List<Ingradients> {
        TODO("Not yet implemented")
    }

    override suspend fun filterByMainIngredient(ingredient: String): List<MainIngradient> {
        TODO("Not yet implemented")
    }

    override suspend fun filterByCategory(category: String): List<Meal> {
        TODO("Not yet implemented")
    }

    override suspend fun filterByArea(area: String): List<Meal> {
        TODO("Not yet implemented")
    }

}