package com.example.task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.MealAPI

class PopularAdapter(val meals:List<MealAPI>) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.meal_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var p = meals.get(position)
        holder.text.text = p.strMeal
        Glide.with(holder.img)
            .load(p.strMealThumb)
            .into(holder.img)

//        holder.itemView.setOnClickListener{
//            onItemClick(p)
//        }
    }

    override fun getItemCount(): Int = meals.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card){
        var text = card.findViewById<TextView>(R.id.meal_title)
        var img = card.findViewById<ImageView>(R.id.meal_pop_image)


    }


}