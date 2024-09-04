package com.example.recipeapp.home.CategoryFragment

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient

class CategoryFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var categoryItemsAdapter: CategoryFragmentAdapter
    private lateinit var loadingAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CategoryFragment", "onCreateView called")
        return inflater.inflate(R.layout.specific_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CategoryFragment", "onViewCreated called")

        // Initialize the loading animation view
        loadingAnimationView = view.findViewById(R.id.loading_animation)

        // Retrieve the data from the bundle
        val categoryTitle = arguments?.getString("title") ?: ""
        val categoryImage = arguments?.getString("image")
        sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
        toolbar.title = categoryTitle
        Log.d("habiba", categoryTitle.toString())
        Log.d("habiba", categoryImage.toString())

        // Find the TextView and ImageView by ID
        val titleTextView = view.findViewById<TextView>(R.id.category_title)
        val imageView = view.findViewById<ImageView>(R.id.category_image)

        // Set the title and image
        titleTextView.text = categoryTitle
        categoryImage?.let {
            Log.d("CategoryFragment", "Loading image with URL: $it")
            Glide.with(this).load(it).into(imageView)
        }

        val CategoryViewModelFactory = CategoryViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()),
                FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )

        val categoriesViewModel = ViewModelProvider(this, CategoryViewModelFactory).get(
            CategoryViewModel::class.java
        )

        // Initialize the Category RecyclerView
        val eachCatItemRecycler = view.findViewById<RecyclerView>(R.id.cat_item_recycler_view)
        categoryItemsAdapter = CategoryFragmentAdapter(
            emptyList(),
            { meal ->
                val action = CategoryFragmentDirections.actionCategoryFragmentToRecipeDetails(meal)
                findNavController().navigate(action)
            },
            { meal ->
                if (categoriesViewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
                    categoriesViewModel.deleteFavourite(meal, userId)
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    categoriesViewModel.insertFavourite(meal, userId)
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        )
        eachCatItemRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        eachCatItemRecycler.adapter = categoryItemsAdapter

        categoriesViewModel.Categorttype.observe(viewLifecycleOwner) { CategoryType ->
            Log.d("CategoryFragment", "Observer triggered with ${CategoryType?.size} items")
            val favoriteIds = categoriesViewModel.favorites.value?.map { it.idMeal }?.toSet() ?: emptySet()
            categoryItemsAdapter.updateData(CategoryType ?: emptyList(), favoriteIds)

            // Hide the loading animation when data is loaded
            loadingAnimationView.cancelAnimation() // Stop the animation
            loadingAnimationView.visibility = View.GONE
            eachCatItemRecycler.visibility = View.VISIBLE
        }

        categoriesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites?.map { it.idMeal }?.toSet() ?: emptySet()
            categoryItemsAdapter.updateData(categoriesViewModel.Categorttype.value ?: emptyList(), favoriteIds)
        }

        // Show the loading animation before starting data fetch
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation() // Start the animation
        eachCatItemRecycler.visibility = View.GONE

        categoriesViewModel.getMealsbyCategory(categoryTitle)
        categoriesViewModel.getFavorites(userId)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            networkInfo.isConnected
        }
    }
}
