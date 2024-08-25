package com.example.recipeapp.database

import androidx.room.Query

interface LocalDataSource {
    suspend fun insert(user:User)
    suspend fun selectById(userId :Int) :User?
    suspend fun selectByEmail(userEmail :String) :User?
}