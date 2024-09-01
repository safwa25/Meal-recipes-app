package com.example.recipeapp.Search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.CategoryFilterClass

class Each_cat_item_RecyclerAdaptor(private var each_cat_item: List<CategoryFilterClass>) : RecyclerView.Adapter<Each_cat_item_RecyclerAdaptor.MyViewHolder>() {

    // ViewHolder class to hold references to the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cat_image_view)
        val title: TextView = itemView.findViewById(R.id.cat_item_title)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val each_item = each_cat_item[position]
        holder.title.text = each_item.strMeal

        Glide.with(holder.image.context)
            .load(each_item.strMealThumb)
            .into(holder.image)



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
    fun updateData(newItems: List<CategoryFilterClass>) {
        each_cat_item = newItems
        Log.d("Each_cat_item_RecyclerAdaptor", "Updating data with ${newItems.size} items")
        notifyDataSetChanged()
    }
}
