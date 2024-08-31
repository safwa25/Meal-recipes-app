package com.example.recipeapp.home.recipe_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.dto.Meal
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class RecipeDetails : Fragment() {
    val args : RecipeDetailsArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_recipe_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mealName=view.findViewById<TextView>(R.id.meal_text)
        var mealImageView=view.findViewById<ImageView>(R.id.meal_image)
        var mealCategory=view.findViewById<TextView>(R.id.category_name)
        var mealInstructions=view.findViewById<TextView>(R.id.recipeInstruction_text)
        var readMoreLessButton=view.findViewById<Button>(R.id.showMoreButton)
        var youTubePlayerView=view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        var isExpand=false

        mealName.text=args.meal.strMeal
        mealCategory.text=args.meal.strCategory

        Glide.with(view)
            .load(args.meal.strMealThumb)
            .into(mealImageView)

        mealInstructions.text = args.meal.strInstructions
        readMoreLessButton.setOnClickListener {
            if (isExpand){
                mealInstructions.maxLines = 3
                readMoreLessButton.text = "Show more"
                isExpand = false
            }
            else{
                mealInstructions.maxLines=Int.MAX_VALUE
                readMoreLessButton.text = "Show less"
                isExpand = true

            }
        }
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val videoId = args.meal.strYoutube?.takeLast(11)
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0F)
                    youTubePlayer.pause()
                } else {
                    youTubePlayerView.isInvisible=true
                }
            }
        })


    }
}