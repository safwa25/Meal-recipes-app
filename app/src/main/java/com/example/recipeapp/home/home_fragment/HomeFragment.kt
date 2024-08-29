package com.example.recipeapp.home.home_fragment

import SpaceItemDecoration
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
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient
import com.example.task2.AreasAdapter
import com.example.task2.PopularAdapter

class HomeFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var randomMealTitle: TextView
    private lateinit var randomMealDescription: TextView
    private var mealAdapter: PopularAdapter? = null // Changed to nullable
    private lateinit var areasAdapter: AreasAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isInternetAvailable(view.context)) {
            val viewModelFactory = HomeViewFactory(
                RepositoryImplement(
                    LocalDataBaseImplement(requireContext()),
                    MealLocalDsImplement(requireContext()),
                    FavouritesLocalDsImplement(requireContext()),
                    APIClient
                )
            )
            val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

            image = view.findViewById(R.id.meal_image)
            randomMealTitle = view.findViewById(R.id.recipeTitle)
            randomMealDescription = view.findViewById(R.id.recipeDesc)


            viewModel.getRandomMeal()
            viewModel.randomMeal.observe(viewLifecycleOwner) {
                Glide.with(view).load(it?.strMealThumb.toString()).into(image)
                randomMealTitle.text = it?.strMeal.toString()
                randomMealDescription.text = it?.strInstructions.toString()
            }

            sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("id", -1)

            val mealsRecyclerView = view.findViewById<RecyclerView>(R.id.popular_rv)
            mealsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mealsRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            val areasRecyclerView = view.findViewById<RecyclerView>(R.id.areas_rv)
            areasRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            areasRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            // Initialize the adapter
            mealAdapter = PopularAdapter(emptyList()) { meal ->
                if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
                    viewModel.deleteFavourite(meal, userId)
                } else {
                    viewModel.insertFavourite(meal, userId)
                }
            }
            mealsRecyclerView.adapter = mealAdapter

            // Observe and update meal list
            viewModel.randomMealList.observe(viewLifecycleOwner) { meals ->
                val favoriteIds = viewModel.favorites.value?.map { it.idMeal }?.toSet() ?: emptySet()
                mealAdapter?.updateData(meals ?: emptyList(), favoriteIds)
            }

            // Observe and update random meal
            viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
                Glide.with(view).load(meal?.strMealThumb).into(image)
                randomMealTitle.text = meal?.strMeal ?: ""
                randomMealDescription.text = meal?.strInstructions ?: ""
            }

            // Observe and update areas
            viewModel.areas.observe(viewLifecycleOwner) { areas ->
                areasAdapter.updateData(areas)
            }

            // Observe and update favorites
            viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
                val favoriteIds = favorites?.map { it.idMeal }?.toSet() ?: emptySet()
                mealAdapter?.updateData(viewModel.randomMealList.value ?: emptyList(), favoriteIds)
            }

            // Trigger initial data fetch
            viewModel.getRandomMealList()
            viewModel.getAreas()
            viewModel.getFavorites(userId)

            areasAdapter = AreasAdapter(viewModel.areas.value ?: emptyList()) { area ->
                val action = HomeFragmentDirections.actionHomeFragmentToAreaFragment(area)
                findNavController().navigate(action)
            }
            areasRecyclerView.adapter = areasAdapter

        } else {
            Log.d("HomeFragment", "No network connection")
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
