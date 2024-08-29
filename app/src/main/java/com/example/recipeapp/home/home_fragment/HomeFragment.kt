package com.example.recipeapp.home.home_fragment

import SpaceItemDecoration
import android.content.ClipDescription
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException


class HomeFragment : Fragment() {
    private lateinit var image :ImageView
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var randomMealTitile: TextView
    private lateinit var randomMealDescription: TextView
    private lateinit var mealAdapter: PopularAdapter
    private lateinit var areasAdapter: AreasAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isInternetAvailable(view.context)) {
            val viewModelFactory = HomeViewFactory(
                RepositoryImplement(
                    LocalDataBaseImplement(this.requireContext()),
                    MealLocalDsImplement(this.requireContext()), FavouritesLocalDsImplement(this.requireContext()),
                    APIClient
                )
            )
            val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
            image=view.findViewById(R.id.meal_image)
            randomMealTitile=view.findViewById(R.id.recipeTitle)
            randomMealDescription=view.findViewById(R.id.recipeDesc)


            viewModel.getRandomMeal()
            viewModel.randomMeal.observe(viewLifecycleOwner) {
                Glide.with(view).load(it?.strMealThumb.toString()).into(image)
                randomMealTitile.text = it?.strMeal.toString()
                randomMealDescription.text = it?.strInstructions.toString()
            }


            sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)

            mealAdapter = PopularAdapter(viewModel.randomMealList.value ?: emptyList()) { meal, fab ->
                val userId = sharedPreferences.getInt("id", -1)
                viewModel.insertFavourite(meal, userId)
                fab.setImageResource(R.drawable.baseline_favorite_24)
                Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
            }
            val mealsRecyclerView = view.findViewById<RecyclerView>(R.id.popular_rv)
            mealsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mealsRecyclerView.adapter = mealAdapter
            mealsRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            viewModel.randomMealList.observe(viewLifecycleOwner) { meals ->
                mealAdapter = PopularAdapter(viewModel.randomMealList.value ?: emptyList()) { meal, fab ->
                    val userId = sharedPreferences.getInt("id", -1)
                    viewModel.insertFavourite(meal, userId)
                    fab.setImageResource(R.drawable.baseline_favorite_24)
                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
                }
                mealsRecyclerView.adapter = mealAdapter
            }


            viewModel.getRandomMealList()


            areasAdapter = AreasAdapter(viewModel.areas.value ?: emptyList()) { area ->
                val action = HomeFragmentDirections.actionHomeFragmentToAreaFragment(area)
                findNavController().navigate(action)
            }
            val areasRecyclerView = view.findViewById<RecyclerView>(R.id.areas_rv)
            areasRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            areasRecyclerView.adapter = areasAdapter
            areasRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            viewModel.randomMealList.observe(viewLifecycleOwner) { meals ->
                areasAdapter = AreasAdapter(viewModel.areas.value ?: emptyList()) { area ->
                    val action = HomeFragmentDirections.actionHomeFragmentToAreaFragment(area)
                    findNavController().navigate(action)
                }
            }

            viewModel.getAreas()






        } else {
            Log.d("asd", "Error")
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
        }


    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}