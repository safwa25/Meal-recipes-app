package com.example.recipeapp.appstorage

import android.util.Log
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.favourites.FavouritesLocalDs
import com.example.recipeapp.database.meal.MealLocalDs
import com.example.recipeapp.database.user.LocalDataSource
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.AreasStr
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Ingradients
import com.example.recipeapp.dto.MainIngradient
import com.example.recipeapp.dto.MealDataBase
import com.example.recipeapp.dto.MealList
import com.example.recipeapp.network.RemoteDataSource
import retrofit2.Response

class RepositoryImplement(val localDataSource: LocalDataSource, val mealLocalDs: MealLocalDs, val favouritesLocalDs: FavouritesLocalDs,val remoteDataSource: RemoteDataSource):Repository {
    override suspend fun insert(user: User) {
        localDataSource.insert(user)
    }

    override suspend fun selectById(userId: Int): User? {
        return localDataSource.selectById(userId)
    }

    override suspend fun selectByEmail(userEmail:String): User? {
        val data = localDataSource.selectByEmail(userEmail)
        Log.d("asd", data.toString())
        return data
    }

    override suspend fun selectByEmailAndPassword(userEmail: String, passwordEmail: String): User? {
        val data1=localDataSource.selectByEmailAndPassword(userEmail,passwordEmail)
        Log.d("asd", data1.toString())
        return data1
    }

    override suspend fun returnlastid(): Int {
        return localDataSource.returnlastid()
    }

    override suspend fun insertMeal(mealDataBase: MealDataBase) {
        mealLocalDs.insertMeal(mealDataBase)
    }

    override suspend fun searchMeals(mealName: String): List<MealDataBase> {
        return mealLocalDs.searchMeals(mealName)
    }

    override suspend fun insertFavourite(favourite: Favourites) {
        favouritesLocalDs.insertFavourite(favourite)
    }

    override suspend fun getFavourites(userId: Int): List<MealDataBase> {
        return favouritesLocalDs.getFavourites(userId)
    }




    override suspend fun searchMealByName(mealName: String): Response<MealList> {
        return remoteDataSource.searchMealByName(mealName)
    }

    override suspend fun listMealsByFirstLetter(firstLetter: String): Response<List<MealDataBase>> {
        return remoteDataSource.listMealsByFirstLetter(firstLetter)
    }

    override suspend fun lookupMealById(mealId: String): Response<MealDataBase> {
        return remoteDataSource.lookupMealById(mealId)
    }

    override suspend fun getRandomMeal(): Response<MealList> {
        return remoteDataSource.getRandomMeal()
    }

    override suspend fun listAllCategories(): Response<List<Category>> {
        return remoteDataSource.listAllCategories()
    }

    override suspend fun listCategories(): Response<List<String>> {
        return remoteDataSource.listCategories()
    }

    override suspend fun listAreas(): Response<AreasStr> {
        return remoteDataSource.listAreas()
    }

    override suspend fun listIngredients(): Response<List<Ingradients>> {
        return remoteDataSource.listIngredients()
    }

    override suspend fun filterByMainIngredient(ingredient: String): Response<List<MainIngradient>> {
        return remoteDataSource.filterByMainIngredient(ingredient)
    }

    override suspend fun filterByCategory(category: String): Response<List<MealDataBase>> {
        return remoteDataSource.filterByCategory(category)
    }

    override suspend fun filterByArea(area: String): Response<List<MealDataBase>> {
        return remoteDataSource.filterByArea(area)
    }

}