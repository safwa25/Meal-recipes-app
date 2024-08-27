package com.example.recipeapp.network

import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.Call

interface APIService {

    // Search meal by name
    @GET("search.php")
    suspend fun searchMealByName(@Query("s") mealName: String): List<Meal>

    // List all meals by first letter
    @GET("search.php")
    suspend fun listMealsByFirstLetter(@Query("f") firstLetter: String): List<Meal>

    // Lookup full meal details by id
    @GET("lookup.php")
    suspend fun lookupMealById(@Query("i") mealId: String): Meal

    // Lookup a single random meal
    @GET("random.php")
    suspend fun getRandomMeal(): Meal

    // List all meal categories
    @GET("categories.php")
    suspend fun listAllCategories(): List<Category>

    // List of all categories names
    @GET("list.php")
    suspend fun listCategories(@Query("c") listType: String = "list"): List<String>

    // List of all areas names
    @GET("list.php")
    suspend fun listAreas(@Query("a") listType: String = "list"): List<String>

    // List of all ingredients names
    @GET("list.php")
    suspend fun listIngredients(@Query("i") listType: String = "list"): List<Ingradients>

    // Filter by main ingredient
    @GET("filter.php")
    suspend fun filterByMainIngredient(@Query("i") ingredient: String): List<MainIngradient>

    // Filter by category
    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): List<Meal>

    // Filter by area
    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): List<Meal>

}
