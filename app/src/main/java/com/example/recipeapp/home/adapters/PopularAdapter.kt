package com.example.task2

import android.app.AlertDialog
import android.content.Context
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

class PopularAdapter(
    private var meals: List<Meal>,
    private val onFavClick: (Meal, FloatingActionButton) -> Unit, private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private var favoriteMeals = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.meal_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.text.text = meal.strMeal
        Glide.with(holder.img)
            .load(meal.strMealThumb)
            .into(holder.img)

        // Update the FAB icon based on whether the meal is in the favorites
        holder.fab.setImageResource(
            if (favoriteMeals.contains(meal.idMeal)) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        holder.fab.setOnClickListener {
            if (favoriteMeals.contains(meal.idMeal)) {
                showConfirmationDialog(holder.itemView.context, meal) { confirmed ->
                    if (confirmed) {
                        onFavClick(meal,holder.fab)
                        favoriteMeals.remove(meal.idMeal)
                        notifyItemChanged(position)
                    }
                }
            } else {
                onFavClick(meal,holder.fab)
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
        val text: TextView = card.findViewById(R.id.meal_title)
        val img: ImageView = card.findViewById(R.id.meal_pop_image)
        val fab: FloatingActionButton = card.findViewById(R.id.pop_fav_btn)
    }

    fun updateData(newMeals: List<Meal>, favoriteIds: Set<String>) {
        meals = newMeals
        favoriteMeals = favoriteIds.toMutableSet() // Update the favorite meals
        notifyDataSetChanged()
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
}
