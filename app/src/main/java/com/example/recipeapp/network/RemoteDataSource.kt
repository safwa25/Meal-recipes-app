package com.example.recipeapp.network
import CategoriesResponse
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealDataBase
import com.example.recipeapp.dto.MealList
import retrofit2.Response

interface RemoteDataSource {
    suspend fun searchMealByName(mealName: String): Response<MealList>

    suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<MealDataBase>>

    suspend fun lookupMealById(mealId: String): Response<MealDataBase>

    suspend fun getRandomMeal(): Response<MealList>

    suspend fun listAllCategories():Response<CategoriesResponse>

    suspend fun listCategories(): Response<List<String>>

    suspend fun listAreas(): Response<AreasStr>

    suspend fun listIngredients(): Response<List<Ingradients>>

    suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>>

    suspend fun filterByCategory(category: String): Response<List<MealDataBase>>

    suspend fun filterByArea(area: String): Response<List<MealDataBase>>
}
