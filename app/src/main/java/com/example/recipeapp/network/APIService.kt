package com.example.recipeapp.network

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
    fun searchMealByName(@Query("s") mealName: String): Meal

    // List all meals by first letter
    @GET("search.php")
    fun listMealsByFirstLetter(@Query("f") firstLetter: String):List<Meal>

    // Lookup full meal details by id
    @GET("lookup.php")
    fun lookupMealById(@Query("i") mealId: String): Meal

    // Lookup a single random meal
    @GET("random.php")
    fun getRandomMeal(): Meal

    // List all meal categories
    @GET("categories.php")
    fun listAllCategories():List<Meal>

    //list of all categories names
    @GET("list.php")
    fun listCategories(@Query("c") listType: String = "list"): List<String>

    //list of all areas names
    @GET("list.php")
    fun listAreas(@Query("a") listType: String = "list"): List<String>

    //list of all ingredients names
    @GET("list.php")
    fun listIngredients(@Query("i") listType: String = "list"): List<Ingradients>

    // Filter by main ingredient
    @GET("filter.php")
    fun filterByMainIngredient(@Query("i") ingredient: String): List<MainIngradient>

    // Filter by Category
    @GET("filter.php")
    fun filterByCategory(@Query("c") category: String): List<Meal>

    // Filter by Area
    @GET("filter.php")
    fun filterByArea(@Query("a") area: String):List<Meal>
}
