package com.example.recipeapp.home.Search

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


class SearchFragmentAdapter(private var searchList: List<Meal>,val onRecipeClick: (Meal) -> Unit) : RecyclerView.Adapter<SearchFragmentAdapter.MyViewHolder>() {

    // ViewHolder class to hold references to the item components
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.Search_image)
        val title: TextView = itemView.findViewById(R.id.recipe_title)
        val ingrd1:TextView=itemView.findViewById(R.id.ingredient1)
        val ingrd2:TextView=itemView.findViewById(R.id.ingredient2)
        val ingrd3:TextView=itemView.findViewById(R.id.ingredient3)
        val heart:FloatingActionButton=itemView.findViewById(R.id.pop_fav_btn)
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

            holder.itemView.setOnClickListener {
                onRecipeClick(searchResponse)
            }
        } else {
            holder.title.text = "No Data"
        }
    }


    // Inflate the item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    // Function to update the data
    fun updateData(newList: List<Meal>) {
        searchList = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

}
