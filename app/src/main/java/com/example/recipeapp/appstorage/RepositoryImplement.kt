package com.example.recipeapp.appstorage

import com.example.recipeapp.database.LocalDataSource
import com.example.recipeapp.database.User

class RepositoryImplement(val localDataSource: LocalDataSource):Repository {
    override suspend fun insert(user: User) {
        localDataSource.insert(user)
    }

    override suspend fun selectById(userId: Int): User? {
        return localDataSource.selectById(userId)
    }

    override suspend fun selectByEmail(userEmail:String ,passwordEmail:String): User? {
        return localDataSource.selectByEmail(userEmail,passwordEmail)
    }


}