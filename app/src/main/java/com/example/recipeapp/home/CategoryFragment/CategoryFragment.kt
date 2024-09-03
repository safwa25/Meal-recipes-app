package com.example.recipeapp.home.CategoryFragment

import android.content.Context
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient

class CategoryFragment : Fragment() {

    private lateinit var categoryItemsAdapter: CategoryFragmentAdapter

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

        // Retrieve the data from the bundle
        val categoryTitle = arguments?.getString("title") ?: ""
        val categoryImage = arguments?.getString("image")
        Log.d("habiba",categoryTitle.toString())
        Log.d("habiba",categoryImage.toString())

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
            CategoryViewModel::class.java)

        // Initialize the Category RecyclerView
        val eachCatItemRecycler = view.findViewById<RecyclerView>(R.id.cat_item_recycler_view)
        categoryItemsAdapter = CategoryFragmentAdapter(emptyList()
        ) { meal_name -> categoriesViewModel.getMealsbyname(meal_name)
            categoriesViewModel.meal.observe(viewLifecycleOwner){returned_meal->
                val action=CategoryFragmentDirections.actionCategoryFragmentToRecipeDetails(returned_meal)
                findNavController().navigate(action)

            }
        }


        eachCatItemRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        eachCatItemRecycler.adapter = categoryItemsAdapter
        Log.d("CategoryFragment", "Recycler view set up with adapter")

        categoriesViewModel.getMealsbyCategory(categoryTitle)

        categoriesViewModel.Categorttype.observe(viewLifecycleOwner) { CategoryType ->
            Log.d("CategoryFragment", "Observer triggered with ${CategoryType?.size} items")
            CategoryType?.let {
                categoryItemsAdapter.updateData(it)
                Log.d("CategoryFragment", "Adapter data updated with ${it.size} items")
            }
        }

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
