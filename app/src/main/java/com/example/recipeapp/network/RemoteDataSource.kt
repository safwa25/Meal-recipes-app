package com.example.recipeapp.network
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.MealDataBase
import retrofit2.Response

interface RemoteDataSource {
    suspend fun searchMealByName(mealName: String): Response<List<MealDataBase>>

    suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<MealDataBase>>

    suspend fun lookupMealById(mealId: String): Response<MealDataBase>

    suspend fun getRandomMeal(): Response<MealDataBase>

    suspend fun listAllCategories(): Response<List<Category>>

    suspend fun listCategories(): Response<List<String>>

    suspend fun listAreas(): Response<List<String>>

    suspend fun listIngredients(): Response<List<Ingradients>>

    suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>>

    suspend fun filterByCategory(category: String): Response<List<MealDataBase>>

    suspend fun filterByArea(area: String): Response<List<MealDataBase>>
}
