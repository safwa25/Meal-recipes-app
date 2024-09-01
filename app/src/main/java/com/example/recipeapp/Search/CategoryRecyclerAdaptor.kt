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

// Define the interface for item click handling
interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}

class CategoryRecyclerAdaptor(
    private var categoryList: List<Category>,
//    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryRecyclerAdaptor.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cat_img_itm)
        val title: TextView = itemView.findViewById(R.id.cat_title_item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryList[position]
        holder.title.text = category.strCategory

        Glide.with(holder.image.context)
            .load(category.strCategoryThumb)
            .into(holder.image)

//        // Handle item clicks
//        holder.itemView.setOnClickListener {
//            listener.onCategoryClick(category)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_layout, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateData(newCategoryList: List<Category>) {
        categoryList = newCategoryList
        notifyDataSetChanged()
    }
}
