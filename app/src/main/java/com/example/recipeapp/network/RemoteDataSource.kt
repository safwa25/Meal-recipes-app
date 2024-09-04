package com.example.recipeapp.network
import CategoryList
import com.example.recipeapp.dto.AreaMealsResponse
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.CategoryFilterList
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
import retrofit2.Response

interface RemoteDataSource {
    suspend fun searchMealByName(mealName: String): Response<MealList>

    suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<Meal>>

    suspend fun lookupMealById(mealId: String): Response<MealList>

    suspend fun getRandomMeal(): Response<MealList>

    suspend fun listAllCategories(): Response<CategoryList>

    suspend fun listCategories(): Response<List<String>>

    suspend fun listAreas(): Response<AreasStr>

    suspend fun listIngredients(): Response<List<Ingradients>>

    suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>>

    suspend fun filterByCategory(category: String): Response<CategoryFilterList>

    suspend fun filterByArea(area: String): Response<AreaMealsResponse>
}
