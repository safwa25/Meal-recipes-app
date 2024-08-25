package com.example.recipeapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user:User)

    @Query("SELECT * FROM user where id= :userId")
            suspend fun selectById(userId :Int) :User?

    @Query("SELECT * FROM user where email= :userEmail and password =:passwordEmail")
    suspend fun selectByEmail(userEmail :String,passwordEmail:String) :User?



}