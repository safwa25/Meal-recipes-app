package com.example.recipeapp.home.aboutus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.dto.Meal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutUsAdapter (var names:Array<String>,var emails: Array<String>,var images:Array<Int>,val onemailClick: (String) -> Unit ) : RecyclerView.Adapter<AboutUsAdapter.ViewHolder>() {

    inner class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.profile_name)
        var email: TextView = row.findViewById(R.id.profile_email)
        var image: ImageView = row.findViewById(R.id.profile_image)

        fun bind(currentname : String,currentemail : String,currentimage : Int) {
            name.setText(currentname)
            email.setText(currentemail)
            image.setImageResource(currentimage)
            itemView.setOnClickListener {
                onemailClick(currentemail)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.personalcard, parent, false)
        return ViewHolder(row)
    }

    override fun getItemCount() = names.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentname =names[position]
        val currentemail =emails[position]
        val currentimage =images[position]
        holder.bind(currentname,currentemail,currentimage)
    }
}
