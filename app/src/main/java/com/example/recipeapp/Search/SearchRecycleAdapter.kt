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
        val ingrd1:TextView=itemView.findViewById(R.id.ingredient1)
        val ingrd2:TextView=itemView.findViewById(R.id.ingredient2)
        val ingrd3:TextView=itemView.findViewById(R.id.ingredient3)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchResponse = searchList.getOrNull(position)
        if (searchResponse != null) {
            holder.title.text = searchResponse.strMeal ?: "No Title"
            holder.ingrd1.text=searchResponse.strIngredient1
            holder.ingrd2.text=searchResponse.strIngredient2
            holder.ingrd3.text=searchResponse.strIngredient3
            // Truncate the description and add "......" if it's longer than 20 characters
//            holder.describ.text = getTruncatedText(searchResponse.strInstructions, 100)

            Glide.with(holder.image.context)
                .load(searchResponse.strMealThumb)
                .into(holder.image)
        } else {
            holder.title.text = "No Data"
//            holder.describ.text = ""
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
