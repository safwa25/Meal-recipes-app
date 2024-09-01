package com.example.recipeapp.network

import CategoriesResponse
import android.util.Log
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.CategoryFilterClass
import com.example.recipeapp.dto.CategoryFilterList
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.MealDataBase
import com.example.recipeapp.dto.MealList
import com.example.recipeapp.dto.SearchList
import retrofit2.Response

object APIClient : RemoteDataSource {

    override suspend fun searchMealByName(mealName: String): Response<SearchList> {
        return API.retrofitService.searchMealByName(mealName)
    }

    override suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<MealDataBase>> {
        return API.retrofitService.listMealsByFirstLetter(firstLetter)
    }

    override suspend fun lookupMealById(mealId: String): Response<MealDataBase> {
        return API.retrofitService.lookupMealById(mealId)
    }

    override suspend fun getRandomMeal(): Response<MealList> {
        return API.retrofitService.getRandomMeal()
    }

    override suspend fun listAllCategories(): Response<CategoriesResponse> {
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
        val Response=API.retrofitService.filterByCategory(category)
        Log.d("asd_habiba",Response.toString())
        return Response
    }

    override suspend fun filterByArea(area: String): Response<List<MealDataBase>> {
        return API.retrofitService.filterByArea(area)
    }
}
