package com.example.recipeapp.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.SearchClass

class SearchRecycleAdapter(private var searchList: List<SearchClass>) : RecyclerView.Adapter<SearchRecycleAdapter.MyViewHolder>() {

    // ViewHolder class to hold references to the item components
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.Search_image)
        val title: TextView = itemView.findViewById(R.id.recipe_title)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchResponse = searchList.getOrNull(position)
        if (searchResponse != null) {
            holder.title.text = searchResponse.strMeal ?: "No Title"

            Glide.with(holder.image.context)
                .load(searchResponse.strMealThumb)
                .into(holder.image)
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
    fun updateData(newList: List<SearchClass>) {
        searchList = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
