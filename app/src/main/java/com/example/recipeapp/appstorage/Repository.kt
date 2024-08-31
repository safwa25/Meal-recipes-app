package com.example.recipeapp.appstorage

import CategoriesResponse
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.MealDataBase
import com.example.recipeapp.dto.MealList
import com.example.recipeapp.dto.SearchList
import retrofit2.Response

interface Repository {
    suspend fun insert(user: User)
    suspend fun selectById(userId :Int) : User?
    suspend fun selectByEmail(userEmail :String) : User?
    suspend fun selectByEmailAndPassword(userEmail :String,passwordEmail:String) : User?
    suspend fun insertMeal(mealDataBase: MealDataBase)
    suspend fun searchMeals(mealName : String): List<MealDataBase>
    suspend fun insertFavourite(favourite: Favourites)
    suspend fun getFavourites(userId: Int): List<MealDataBase>


    suspend fun searchMealByName(mealName: String): Response<SearchList>
    suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<MealDataBase>>
    suspend fun lookupMealById(mealId: String): Response<MealDataBase>
    suspend fun getRandomMeal(): Response<MealList>
    suspend fun returnlastid():Int

    suspend fun listAllCategories(): Response<CategoriesResponse>

    suspend fun listCategories(): Response<List<String>>

    suspend fun listAreas(): Response<AreasStr>

    suspend fun listIngredients(): Response<List<Ingradients>>

    suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>>

    suspend fun filterByCategory(category: String): Response<List<MealDataBase>>

    suspend fun filterByArea(area: String): Response<List<MealDataBase>>

}