package com.example.recipeapp.network

import CategoryList
import android.util.Log
import com.example.recipeapp.dto.AreaMealsResponse
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.CategoryFilterList
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealList
import retrofit2.Response

object APIClient : RemoteDataSource {

    override suspend fun searchMealByName(mealName: String): Response<MealList> {
        return API.retrofitService.searchMealByName(mealName)
    }

    override suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<Meal>> {
        return API.retrofitService.listMealsByFirstLetter(firstLetter)
    }

    override suspend fun lookupMealById(mealId: String): Response<MealList> {
        return API.retrofitService.lookupMealById(mealId)

    }

    override suspend fun getRandomMeal(): Response<MealList> {
        return API.retrofitService.getRandomMeal()
    }

    override suspend fun listAllCategories(): Response<CategoryList> {
        return API.retrofitService.listAllCategories()
    }

    override suspend fun listCategories(): Response<List<String>> {
        return API.retrofitService.listCategories()
    }

    override suspend fun listAreas(): Response<AreasStr> {
        return API.retrofitService.listAreas()
    }

    override suspend fun listIngredients(): Response<List<Ingradients>> {
        return API.retrofitService.listIngredients()
    }

    override suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>> {
        return API.retrofitService.filterByMainIngredient(ingredient)
    }

    override suspend fun filterByCategory(category: String): Response<CategoryFilterList> {
        return API.retrofitService.filterByCategory(category)
    }

    override suspend fun filterByArea(area: String): Response<AreaMealsResponse> {
        return API.retrofitService.filterByArea(area)
    }
}
