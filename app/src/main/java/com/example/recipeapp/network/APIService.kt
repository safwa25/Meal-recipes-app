package com.example.recipeapp.network

import CategoriesResponse
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.CategoryFilterClass
import com.example.recipeapp.dto.CategoryFilterList
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.MealDataBase
import com.example.recipeapp.dto.MealList
import com.example.recipeapp.dto.SearchList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface APIService {

    // Search meal by name
    @GET("api/json/v1/1/search.php")
    suspend fun searchMealByName(@Query("s") mealName: String): Response<SearchList>

    // List all meals by first letter
    @GET("search.php")
    suspend fun listMealsByFirstLetter(@Query("f") firstLetter: String): Response<List<MealDataBase>>

    // Lookup full meal details by id
    @GET("lookup.php")
    suspend fun lookupMealById(@Query("i") mealId: String): Response<MealDataBase>

    // Lookup a single random meal
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomMeal(): Response<MealList>

    // List all meal categories
    @GET("api/json/v1/1/categories.php")
    suspend fun listAllCategories(): Response<CategoriesResponse>

    // List of all categories names
    @GET("list.php")
    suspend fun listCategories(@Query("c") listType: String = "list"): Response<List<String>>

    // List of all areas names
    @GET("api/json/v1/1/list.php")
    suspend fun listAreas(@Query("a") listType: String = "list"): Response<AreasStr>

    // List of all ingredients names
    @GET("list.php")
    suspend fun listIngredients(@Query("i") listType: String = "list"): Response<List<Ingradients>>

    // Filter by main ingredient
    @GET("filter.php")
    suspend fun filterByMainIngredient(@Query("i") ingredient: String): Response<List<MainIngradient>>

    // Filter by category
    @GET("api/json/v1/1/filter.php")
    suspend fun filterByCategory(@Query("c") category: String): Response<CategoryFilterList>

    // Filter by area
    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): Response<List<MealDataBase>>

}
