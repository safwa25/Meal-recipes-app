package com.example.recipeapp.home.adapters

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
import com.example.recipeapp.dto.MealArea
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouritesAdapter(var meals:List<Meal>, val onClick: (Meal) -> Unit) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var p = meals.get(position)
        holder.text.text = p.strMeal
        Glide.with(holder.imageView)
            .load(p.strMealThumb)
            .into(holder.imageView)
        holder.fab.setImageResource(R.drawable.baseline_favorite_24)
        holder.fab.setOnClickListener {
            showConfirmationDialog(holder.itemView.context, p) { confirmed ->
                if (confirmed) {
                    onClick(p)
                    notifyItemChanged(position)
                }

            }
        }
    }

    override fun getItemCount(): Int = meals.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card){
        var text = card.findViewById<TextView>(R.id.recipeTitle)
        var imageView = card.findViewById<ImageView>(R.id.meal_image)
        var fab = card.findViewById<FloatingActionButton>(R.id.fav_btn)
    }

    fun updateData(newAreas: List<Meal>) {
        meals = newAreas
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