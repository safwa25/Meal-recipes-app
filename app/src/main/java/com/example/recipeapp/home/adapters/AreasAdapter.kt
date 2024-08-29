package com.example.task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.MealArea

class AreasAdapter(var areas:List<MealArea>, val onAreaClick: (String) -> Unit) : RecyclerView.Adapter<AreasAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.area_card, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var p = areas.get(position)
        holder.text.text = p.strArea
        holder.itemView.setOnClickListener{
            onAreaClick(p.strArea)
        }
    }

    override fun getItemCount(): Int = areas.size

    class ViewHolder(val card: View) : RecyclerView.ViewHolder(card){
        var text = card.findViewById<TextView>(R.id.areaTv)
    }

    fun updateData(newAreas: List<MealArea>) {
        areas = newAreas
        notifyDataSetChanged()
    }



}