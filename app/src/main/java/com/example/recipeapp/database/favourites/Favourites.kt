package com.example.recipeapp.database.favourites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.example.recipeapp.database.user.User
import com.example.recipeapp.dto.Meal


@Entity(tableName = "favourites", primaryKeys = ["mealId", "userId"], foreignKeys = [ForeignKey(entity = Meal::class, parentColumns = ["idMeal"], childColumns = ["mealId"], onDelete = CASCADE) , ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = CASCADE)])
data class Favourites(
    val mealId: String,
    val userId: Int
)
