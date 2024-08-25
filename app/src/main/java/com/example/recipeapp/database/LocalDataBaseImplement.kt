package com.example.recipeapp.database

import android.content.Context

class LocalDataBaseImplement(context: Context) : LocalDataSource {
    private val dao:UserDao
    init {
        val dp= AppDatabase.getDatabase(context)
        dao =dp.userDao()
    }
    override suspend fun insert(user: User) {
        dao.insert(user)
    }

    override suspend fun selectById(userId: Int): User? {
        return dao.selectById(userId)
    }

    override suspend fun selectByEmail(userEmail: String): User? {
        return dao.selectByEmail(userEmail)
    }

}