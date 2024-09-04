package com.example.recipeapp.home.CategoryFragment


import android.app.AlertDialog
import android.content.Context
import android.util.Log
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
import java.util.concurrent.atomic.AtomicBoolean


class CategoryFragmentAdapter(
    private var each_cat_item: List<Meal>,
    val onClick: (Meal) -> Unit,
    private val onFavClick: (Meal) -> Unit
) : RecyclerView.Adapter<CategoryFragmentAdapter.MyViewHolder>() {

    // ViewHolder class to hold references to the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cat_image_view)
        val title: TextView = itemView.findViewById(R.id.cat_item_title)
        var fav: FloatingActionButton = itemView.findViewById(R.id.cat_fav_btn)
        var isProcessing = AtomicBoolean(false) // Track click state
    }

    private var favoriteMeals = mutableSetOf<String>()

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val each_item = each_cat_item[position]
        holder.title.text = each_item.strMeal

        Glide.with(holder.image.context)
            .load(each_item.strMealThumb)
            .into(holder.image)

        holder.fav.setImageResource(
            if (favoriteMeals.contains(each_item.idMeal)) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        holder.fav.setOnClickListener {
            if (favoriteMeals.contains(each_item.idMeal)) {
                showConfirmationDialog(holder.itemView.context, each_item) { confirmed ->
                    if (confirmed) {
                        onFavClick(each_item)
                        favoriteMeals.remove(each_item.idMeal)
                        notifyItemChanged(position)
                    }
                }
            } else {
                onFavClick(each_item)
                favoriteMeals.add(each_item.idMeal)
                notifyItemChanged(position)
            }
        }

        holder.itemView.setOnClickListener {
            onClick(each_item)
        }
    }

    // Inflate the item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.each_cat_sample, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return each_cat_item.size
    }

    // Update the data in the adapter
    fun updateData(newItems: List<Meal>) {
        each_cat_item = newItems
        Log.d("Each_cat_item_RecyclerAdaptor", "Updating data with ${newItems.size} items")
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

    fun updateData(newMeals: List<Meal>, favoriteIds: Set<String>) {
        each_cat_item = newMeals
        favoriteMeals = favoriteIds.toMutableSet() // Update the favorite meals
        notifyDataSetChanged()
    }
}