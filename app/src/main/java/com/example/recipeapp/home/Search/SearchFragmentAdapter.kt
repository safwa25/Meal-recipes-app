package com.example.recipeapp.home.Search

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.Meal
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SearchFragmentAdapter(private var searchList: List<Meal>,private val onFavClick: (Meal) -> Unit,val onRecipeClick: (Meal) -> Unit) : RecyclerView.Adapter<SearchFragmentAdapter.MyViewHolder>() {

    // ViewHolder class to hold references to the item components
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.Search_image)
        val title: TextView = itemView.findViewById(R.id.recipe_title)
        val ingrd1:TextView=itemView.findViewById(R.id.ingredient1)
        val ingrd2:TextView=itemView.findViewById(R.id.ingredient2)
        val ingrd3:TextView=itemView.findViewById(R.id.ingredient3)
        val fav:FloatingActionButton=itemView.findViewById(R.id.pop_fav_btn)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchResponse = searchList.getOrNull(position)
        if (searchResponse != null) {
            holder.title.text = searchResponse.strMeal ?: "No Title"
            holder.ingrd1.text=searchResponse.strIngredient1
            holder.ingrd2.text=searchResponse.strIngredient2
            holder.ingrd3.text=searchResponse.strIngredient3

            Glide.with(holder.image.context)
                .load(searchResponse.strMealThumb)
                .into(holder.image)



            holder.fav.setImageResource(
                if (favoriteMeals.contains(searchResponse.idMeal)) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            holder.fav.setOnClickListener {
                Log.d("Favorites", "Clicked on fav for ${searchResponse.idMeal}")

                if (favoriteMeals.contains(searchResponse.idMeal)) {
                    showConfirmationDialog(holder.itemView.context, searchResponse) { confirmed ->
                        Log.d("Favorites", "Dialog confirmed: $confirmed")

                        if (confirmed) {
                            Log.d("Favorites", "Removing ${searchResponse.idMeal} from favorites")
                            onFavClick(searchResponse)
                            favoriteMeals.remove(searchResponse.idMeal)
                            Log.d("Favorites", "Current favorites after removal: $favoriteMeals")
                            notifyDataSetChanged()  // Consider using this to refresh the entire list
                        }
                    }
                } else {
                    Log.d("Favorites", "Adding ${searchResponse.idMeal} to favorites")
                    onFavClick(searchResponse)
                    favoriteMeals.add(searchResponse.idMeal)
                    Log.d("Favorites", "Current favorites after addition: $favoriteMeals")
                    notifyDataSetChanged()  // Consider using this to refresh the entire list
                }
            }

            holder.itemView.setOnClickListener {
                onRecipeClick(searchResponse)
            }


        } else {
            holder.title.text = "No Data"
        }


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


    // Inflate the item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
    private var favoriteMeals = mutableSetOf<String>()
    // Function to update the data
    fun updateData(newList: List<Meal>,Favourites_set:Set<String>) {
        searchList = newList
        favoriteMeals=Favourites_set.toMutableSet()
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

}
