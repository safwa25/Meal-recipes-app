package com.example.recipeapp.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user where id= :userId")
            suspend fun selectById(userId :Int) : User?

    @Query("SELECT id FROM user ORDER BY id DESC LIMIT 1 ")
    suspend fun returnlastid() : Int

    @Query("SELECT * FROM user where email= :userEmail and password =:passwordEmail")
    suspend fun selectByEmailAndPassword(userEmail :String,passwordEmail:String) : User?

    @Query("SELECT * FROM user where email= :userEmail")
    suspend fun selectByEmail(userEmail :String) : User?




}