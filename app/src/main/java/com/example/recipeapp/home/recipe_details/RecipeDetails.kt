package com.example.recipeapp.home.recipe_details

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.network.APIClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class RecipeDetails : Fragment() {
    val args : RecipeDetailsArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_recipe_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModelFactory=DetailsViewModelFactory(repo = RepositoryImplement(
            remoteDataSource = APIClient,
            localDataSource = LocalDataBaseImplement(this.requireContext()),
            mealLocalDs = MealLocalDsImplement(this.requireContext()),
            favouritesLocalDs = FavouritesLocalDsImplement(this.requireContext())
        ))
        val detailsViewModel=ViewModelProvider(this,viewModelFactory).get(DetailsViewModel::class.java)
        sharedPreferences=requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)


        super.onViewCreated(view, savedInstanceState)
        var favButton=view.findViewById<FloatingActionButton>(R.id.fav_details)
        var mealName=view.findViewById<TextView>(R.id.meal_text)
        var mealImageView=view.findViewById<ImageView>(R.id.meal_image)
        var mealCategory=view.findViewById<TextView>(R.id.category_name)
        var mealInstructions=view.findViewById<TextView>(R.id.recipeInstruction_text)
        var readMoreLessButton=view.findViewById<Button>(R.id.showMoreButton)
        var youTubePlayerView=view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        var isExpand=false

        mealName.text=args.meal.strMeal
        mealCategory.text=args.meal.strCategory
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
        toolbar.title ="${mealName.text} Recipe"

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
        detailsViewModel.getUserFavourites(userId,args.meal.idMeal)

        detailsViewModel.userFavorites.observe(viewLifecycleOwner){ isFavorite->
            if (isFavorite){
                favButton.setImageResource(R.drawable.baseline_favorite_24)
            }else
            {
                favButton.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            favButton.setOnClickListener{
                if (isFavorite){
                    val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    builder.setTitle("Remove Favorite")
                    builder.setMessage("Are you sure you want to remove ${args.meal.strMeal} meal from your favorites?")
                    builder.setPositiveButton("Yes") { dialog, _ ->
                        detailsViewModel.deleteUserFavourite(args.meal.idMeal, userId)
                        favButton.setImageResource(R.drawable.baseline_favorite_border_24)
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
                else {
                    detailsViewModel.insertFavourite(args.meal, userId)
                }
            }

        }
        if (!isNetworkAvailable()) {
            youTubePlayerView.visibility = View.GONE
            Toast.makeText(requireContext(), "No network available ,Can't load the video", Toast.LENGTH_LONG).show()
        } else {
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    val videoId = args.meal.strYoutube?.takeLast(11)
                    if (videoId != null) {
                        youTubePlayer.loadVideo(videoId, 0F)
                        youTubePlayer.pause()
                    } else {
                        youTubePlayerView.isInvisible = true
                    }
                }
            })
        }

    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }
}