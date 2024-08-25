package com.example.recipeapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "user",
        indices = [Index(value = ["email"], unique = true)]
)
data class User  (
    @PrimaryKey(autoGenerate = true) val id : Int  = 0,
    val email : String,
    val password:String,
    val name:String
)