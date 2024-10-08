package com.example.recipeapp.home.favorite_fargment

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.Meal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteAdapter(
    var meals: List<Meal>,
    val onFavClick: (Meal, FloatingActionButton) -> Unit,
    val Internet:Boolean,
    val onRecipeClick: (Meal) -> Unit,
    val navControl:NavController
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var mealname: TextView = row.findViewById(R.id.recipeTitle)
        var image: ImageView = row.findViewById(R.id.meal_image)
        var fab: FloatingActionButton = row.findViewById(R.id.fav_btn)

        fun bind(meal: Meal) {

            mealname.text = meal.strMeal
        if(Internet) {
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.program_background)
                .into(image)
        }else
        {
            image.setImageResource(R.drawable.program_background)
        }

            fab.setImageResource(R.drawable.baseline_favorite_24)
            fab.setOnClickListener {
                onFavClick(meal, fab)
            }
            itemView.setOnClickListener {
                onRecipeClick(meal)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return ViewHolder(row)
    }

    override fun getItemCount() = meals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMeal = meals[position]
        holder.bind(currentMeal)
    }
}
