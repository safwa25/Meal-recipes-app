package com.example.recipeapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.recipeapp.database.favourites.Favourites
import com.example.recipeapp.database.favourites.FavouritesDao
import com.example.recipeapp.database.meal.MealDao
import com.example.recipeapp.database.user.User
import com.example.recipeapp.database.user.UserDao
import com.example.recipeapp.dto.MealDataBase

@Database(entities = [User::class, MealDataBase::class, Favourites::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun favoriteDao(): FavouritesDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context):
                AppDatabase = instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user"
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}
