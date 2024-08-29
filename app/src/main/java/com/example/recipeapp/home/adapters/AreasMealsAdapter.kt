package com.example.task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.AreaMeal
import com.example.recipeapp.dto.MealArea
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AreasMealsAdapter(val areasMeals:List<AreaMeal>) : RecyclerView.Adapter<AreasMealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var p = areasMeals.get(position)
        holder.title.text = p?.strMeal
        Glide.with(holder.image)
            .load(p?.strMealThumb)
            .into(holder.image)


    }

    override fun getItemCount(): Int = areasMeals.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card){
        var title= card.findViewById<TextView>(R.id.recipeTitle)
        var image = card.findViewById<ImageView>(R.id.meal_image)
        var desc = card.findViewById<TextView>(R.id.recipeDesc)
        var fav = card.findViewById<FloatingActionButton>(R.id.fav_btn)

    }


}