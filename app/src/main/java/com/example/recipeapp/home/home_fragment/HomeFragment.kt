package com.example.recipeapp.home.home_fragment

import SpaceItemDecoration
import android.app.AlertDialog
import android.app.ProgressDialog.show
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.network.APIClient
import com.example.task2.AreasAdapter
import com.example.task2.PopularAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var randomMealTitle: TextView
    private lateinit var mealAdapter: PopularAdapter
    private lateinit var areasAdapter: AreasAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var  scaleAnimation : Animation
    private lateinit var favBtn: FloatingActionButton
    private lateinit var animition_constraint: ConstraintLayout
    private lateinit var random_card_var: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scaleAnimation = AnimationUtils.loadAnimation(view.context, R.anim.fab_clicked)
        animition_constraint=view.findViewById(R.id.animation_constaint)
        random_card_var=view.findViewById(R.id.random_card)

        if (isInternetAvailable(view.context)) {
            animition_constraint.visibility=View.GONE
            random_card_var.visibility = View.VISIBLE

            val viewModelSharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val viewModelFactory = HomeViewFactory(
                RepositoryImplement(
                    LocalDataBaseImplement(requireContext()),
                    MealLocalDsImplement(requireContext()),
                    FavouritesLocalDsImplement(requireContext()),
                    APIClient
                ), viewModelSharedPreferences
            )
            val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
            val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.tool_bar)
            toolbar.title ="Welcome,User"

            image = view.findViewById(R.id.meal_image)
            randomMealTitle = view.findViewById(R.id.recipeTitle)
            favBtn = view.findViewById(R.id.fav_btn)
            sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("id", -1)
            viewModel.getUserById(userId)
            viewModel.currentUser.observe(viewLifecycleOwner)
            {user->
                toolbar.title ="Welcome, ${user.name.replaceFirstChar { it.uppercase() }} "
            }
            if(viewModel.randomMeal.value == null) {
                viewModel.getRandomMeal()
            }
            viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
                Glide.with(view).load(meal?.strMealThumb).into(image)
                randomMealTitle.text = meal?.strMeal ?: ""

                // Update the FAB icon based on whether the meal is in the favorites
                favBtn.setImageResource(
                    if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal?.idMeal) == true) {
                        R.drawable.baseline_favorite_24
                    } else {
                        R.drawable.baseline_favorite_border_24
                    }
                )
                val cardLayout = view.findViewById<ViewGroup>(R.id.random_card)

                cardLayout.setOnClickListener{
                    val action = meal?.let { it1 ->
                        HomeFragmentDirections.actionFragmentHomeToRecipeDetails(
                            it1
                        )
                    }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                }


                // Handle the fav button click
                favBtn.setOnClickListener {

                    meal?.let {
                        if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
                            showConfirmationDialog(meal) { confirmed ->
                                if (confirmed) {
                                    viewModel.deleteFavourite(meal, userId)
                                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                                    favBtn.setImageResource(R.drawable.baseline_favorite_border_24)
                                }
                            }
                        } else {
                            favBtn.startAnimation(scaleAnimation)
                            viewModel.insertFavourite(meal, userId)
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                            favBtn.setImageResource(R.drawable.baseline_favorite_24)
                        }
                    }
                }
            }




            val mealsRecyclerView = view.findViewById<RecyclerView>(R.id.popular_rv)
            mealsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mealsRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            val areasRecyclerView = view.findViewById<RecyclerView>(R.id.areas_rv)
            areasRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            areasRecyclerView.addItemDecoration(SpaceItemDecoration(20))

            // Initialize the adapter
            mealAdapter = PopularAdapter(emptyList(), { meal, fab ->
                if (viewModel.favorites.value?.map { it.idMeal }?.contains(meal.idMeal) == true) {
                    viewModel.deleteFavourite(meal, userId)
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    fab.startAnimation(scaleAnimation)
                    viewModel.insertFavourite(meal, userId)
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                }


            }, { meal ->
                val action = HomeFragmentDirections.actionFragmentHomeToRecipeDetails(meal)
                findNavController().navigate(action)
            })
            mealsRecyclerView.adapter = mealAdapter

            // Observe and update meal list
            viewModel.randomMealList.observe(viewLifecycleOwner) { meals ->
                val favoriteIds = viewModel.favorites.value?.map { it.idMeal }?.toSet() ?: emptySet()
                mealAdapter.updateData(meals ?: emptyList(), favoriteIds)
            }

            // Observe and update random meal
            viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
                Glide.with(view).load(meal?.strMealThumb).into(image)
                randomMealTitle.text = meal?.strMeal ?: ""
            }

            // Observe and update areas
            viewModel.areas.observe(viewLifecycleOwner) { areas ->
                areasAdapter.updateData(areas)
            }

            // Observe and update favorites
            viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
                val favoriteIds = favorites?.map { it.idMeal }?.toSet() ?: emptySet()
                mealAdapter.updateData(viewModel.randomMealList.value ?: emptyList(), favoriteIds)
                viewModel.randomMeal.value?.let { meal ->
                    favBtn.setImageResource(
                        if (favoriteIds.contains(meal.idMeal)) {
                            R.drawable.baseline_favorite_24
                        } else {
                            R.drawable.baseline_favorite_border_24
                        }
                    )
                }
            }

            areasAdapter = AreasAdapter(viewModel.areas.value ?: emptyList()) { area ->
                val action = HomeFragmentDirections.actionHomeFragmentToAreaFragment(area)
                findNavController().navigate(action)
            }
            areasRecyclerView.adapter = areasAdapter

            viewModel.getRandomMealList()
            viewModel.getAreas()
            viewModel.getFavorites(userId)



        } else {
            Log.d("HomeFragment", "No network connection")
            animition_constraint.visibility=View.VISIBLE
            random_card_var.visibility = View.GONE
            // Show Snackbar
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                .setAction("Go To Favorites") {
                    findNavController().navigate(R.id.favorite)
                }
                .show();
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes") { _, _ ->
                        requireActivity().finishAffinity() // Exit the app
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }

    // Function to show confirmation dialog
    private fun showConfirmationDialog(meal: Meal, onConfirmed: (Boolean) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Favorite")
            .setMessage("Are you sure you want to remove this item from favorites?")
            .setPositiveButton("Yes") { _, _ ->
                onConfirmed(true)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                onConfirmed(false)
            }
            .create()
            .show()
    }
}
