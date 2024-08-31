package com.example.task2

import android.app.AlertDialog
import android.content.Context
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
    private var meals: List<Meal>,private val onFavClick: (Meal) -> Unit, private val onItemClick: (Meal) -> Unit

) : RecyclerView.Adapter<AreasMealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return ViewHolder(row)
    }
    private var favoriteMeals = mutableSetOf<String>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.title.text = meal?.strMeal ?: "No title available"
        Glide.with(holder.image)
            .load(meal?.strMealThumb)
            .into(holder.image)

        holder.fav.setImageResource(
            if (favoriteMeals.contains(meal.idMeal)) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        holder.fav.setOnClickListener {
            if (favoriteMeals.contains(meal.idMeal)) {
                showConfirmationDialog(holder.itemView.context, meal) { confirmed ->
                    if (confirmed) {
                        onFavClick(meal)
                        favoriteMeals.remove(meal.idMeal)
                        notifyItemChanged(position)
                    }
                }
            } else {
                onFavClick(meal)
                favoriteMeals.add(meal.idMeal)
                notifyItemChanged(position)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(meal)
        }
    }


    override fun getItemCount(): Int = meals.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card) {
        var title = card.findViewById<TextView>(R.id.recipeTitle)
        var image = card.findViewById<ImageView>(R.id.meal_image)
        var fav = card.findViewById<FloatingActionButton>(R.id.fav_btn)
    }

    private fun showConfirmationDialog(context: Context, meal: Meal, onConfirmed: (Boolean) -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Remove Favorite")
            .setMessage("Are you sure you want to remove this item from favorites?")
            .setPositiveButton("Yes") { _, _ ->
                onConfirmed(true)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                onConfirmed(false)
            }
            .create()
            .show()
    }

    fun updateData(newMeals: List<Meal>, favoriteIds: Set<String>) {
        meals = newMeals
        favoriteMeals = favoriteIds.toMutableSet() // Update the favorite meals
        notifyDataSetChanged()
    }
}


