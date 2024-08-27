package com.example.recipeapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.recipeapp.database.user.User
import com.example.recipeapp.database.user.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

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
