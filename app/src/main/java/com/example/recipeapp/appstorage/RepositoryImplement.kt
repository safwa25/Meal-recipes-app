package com.example.recipeapp.appstorage

import android.util.Log
import com.example.recipeapp.database.LocalDataSource
import com.example.recipeapp.database.User

class RepositoryImplement(val localDataSource: LocalDataSource):Repository {
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


}