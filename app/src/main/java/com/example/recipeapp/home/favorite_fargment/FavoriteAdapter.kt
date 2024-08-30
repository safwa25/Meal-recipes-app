package com.example.recipeapp.home.favorite_fargment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.Meal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteAdapter(var meals:List<Meal>,val onFavClick : (Meal, FloatingActionButton) -> Unit) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(val row : View): RecyclerView.ViewHolder(row)
    {
        var mealname = row.findViewById<TextView>(R.id.meal_title)
        var image = row.findViewById<ImageView>(R.id.meal_pop_image)
        var fab = row.findViewById<FloatingActionButton>(R.id.pop_fav_btn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.meal_card, parent, false)
        return ViewHolder(row)
    }

    override fun getItemCount()= meals.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentmeal=meals[position]
        holder.mealname.setText(currentmeal.strMeal)
            Glide.with(holder.itemView.context).load(meals[position].strMealThumb).placeholder(R.drawable.ic_launcher_foreground).into(holder.image)
        holder.fab.setImageResource(R.drawable.baseline_favorite_24)
        holder.fab.setOnClickListener {
            onFavClick(currentmeal, holder.fab)
        }
    }



}