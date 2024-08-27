package com.example.recipeapp.network
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal

interface RemoteDataSource {
    suspend fun searchMealByName(mealName: String): List<Meal>

    suspend fun listMealsByFirstLetter(firstLetter: String): List<Meal>

    suspend fun lookupMealById(mealId: String): Meal

    suspend fun getRandomMeal(): Meal

    suspend fun listAllCategories(): List<Category>

    suspend fun listCategories(): List<String>

    suspend fun listAreas(): List<String>

    suspend fun listIngredients(): List<Ingradients>

    suspend fun filterByMainIngredient(ingredient: String): List<MainIngradient>

    suspend fun filterByCategory(category: String): List<Meal>

    suspend fun filterByArea(area: String): List<Meal>
}