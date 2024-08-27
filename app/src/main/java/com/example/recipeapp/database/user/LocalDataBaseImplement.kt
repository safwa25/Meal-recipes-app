package com.example.recipeapp.database.user

import android.content.Context
import com.example.recipeapp.database.AppDatabase

class LocalDataBaseImplement(context: Context) : LocalDataSource {
    private val dao: UserDao
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

    override suspend fun selectByEmailAndPassword(userEmail: String, passwordEmail: String): User? {
        return dao.selectByEmailAndPassword(userEmail,passwordEmail)
    }

}