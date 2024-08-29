package com.example.task2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.AreaMeal
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.dto.MealArea
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AreasMealsAdapter(
    private val areasMeals: List<AreaMeal>,
    private val getMeal: (String, (Meal?) -> Unit) -> Unit
) : RecyclerView.Adapter<AreasMealsAdapter.ViewHolder>() {

    private val mealCache = mutableMapOf<String, Meal?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val areaMeal = areasMeals[position]


        val cachedMeal = mealCache[areaMeal.idMeal.toString()]
        if (cachedMeal != null) {
            bindMeal(holder, cachedMeal)
        } else {
            getMeal(areaMeal.idMeal.toString()) { meal ->
                mealCache[areaMeal.idMeal.toString()] = meal
                bindMeal(holder, meal)
            }
        }
    }

    private fun bindMeal(holder: ViewHolder, meal: Meal?) {
        holder.title.text = meal?.strMeal ?: "No title available"
        Glide.with(holder.image)
            .load(meal?.strMealThumb)
            .into(holder.image)
        holder.desc.text = meal?.strInstructions ?: "No description available"
    }

    override fun getItemCount(): Int = areasMeals.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card) {
        var title = card.findViewById<TextView>(R.id.recipeTitle)
        var image = card.findViewById<ImageView>(R.id.meal_image)
        var desc = card.findViewById<TextView>(R.id.recipeDesc)
        var fav = card.findViewById<FloatingActionButton>(R.id.fav_btn)
    }
}

