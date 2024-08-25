package com.example.recipeapp.appstorage

import com.example.recipeapp.database.User

interface Repository {
    suspend fun insert(user: User)
    suspend fun selectById(userId :Int) : User?
    suspend fun selectByEmail(userEmail :String) : User?
}