package com.example.recipeapp.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.Category

class CategoryRecyclerAdaptor(private var categoryList: List<Category>) : RecyclerView.Adapter<CategoryRecyclerAdaptor.MyViewHolder>() {

    // ViewHolder class to hold references to the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cat_img_itm)
        val title: TextView = itemView.findViewById(R.id.cat_title_item)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryList[position]
        holder.title.text = category.strCategory

        Glide.with(holder.image.context)
            .load(category.strCategoryThumb)
            .into(holder.image)

        // Handle item clicks
        holder.itemView.setOnClickListener {
            // Implement your filtering logic here
        }
    }

    // Inflate the item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    // Update the data list and refresh the RecyclerView
    fun updateData(newCategoryList: List<Category>) {
        categoryList = newCategoryList
        notifyDataSetChanged()
    }
}
