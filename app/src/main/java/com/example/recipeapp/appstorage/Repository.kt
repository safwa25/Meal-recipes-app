package com.example.recipeapp.appstorage

import CategoryList
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.AreaMealsResponse
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.CategoryFilterList
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
import retrofit2.Response

interface Repository {
    // user database functions
    suspend fun insert(user: User)
    suspend fun selectById(userId :Int) : User?
    suspend fun selectByEmail(userEmail :String) : User?
    suspend fun returnlastid():Int
    suspend fun selectByEmailAndPassword(userEmail :String,passwordEmail:String) : User?
    // meal database functions
    suspend fun insertMeal(mealDataBase: Meal)
    suspend fun searchMeals(mealName : String): List<Meal>
    // favourite database functions
    suspend fun insertFavourite(favourite: Favourites)
    suspend fun getFavourites(userId: Int): List<Meal>
    suspend fun deleteFavouriteByMealId(mealId: String,userId: Int)
    suspend fun checkMeal(mealId: String,userId: Int) : String?
    // api functions
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