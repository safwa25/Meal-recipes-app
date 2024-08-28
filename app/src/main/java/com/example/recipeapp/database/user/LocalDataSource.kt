package com.example.recipeapp.database.user

interface LocalDataSource {
    suspend fun insert(user: User)
    suspend fun selectById(userId :Int) : User?
    suspend fun selectByEmail(userEmail :String) : User?
    suspend fun selectByEmailAndPassword(userEmail :String,passwordEmail:String) : User?
    suspend fun returnlastid():Int
}