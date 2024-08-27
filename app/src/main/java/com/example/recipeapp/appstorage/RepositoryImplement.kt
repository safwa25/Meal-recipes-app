package com.example.recipeapp.appstorage

import android.util.Log
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.favourites.FavouritesLocalDs
import com.example.recipeapp.database.meal.MealLocalDs
import com.example.recipeapp.database.user.LocalDataSource
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.Meal

class RepositoryImplement(val localDataSource: LocalDataSource, val mealLocalDs: MealLocalDs, val favouritesLocalDs: FavouritesLocalDs):Repository {
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

    override suspend fun insertMeal(meal: Meal) {
        mealLocalDs.insertMeal(meal)
    }

    override suspend fun searchMeals(mealName: String): List<Meal> {
        return mealLocalDs.searchMeals(mealName)
    }

    override suspend fun insertFavourite(favourite: Favourites) {
        favouritesLocalDs.insertFavourite(favourite)
    }

    override suspend fun getFavourites(userId: Int): List<Meal> {
        return favouritesLocalDs.getFavourites(userId)
    }


}