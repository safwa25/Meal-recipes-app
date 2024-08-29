package com.example.recipeapp.Search

import SpaceItemDecoration
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.network.APIClient
import com.example.task2.AreasAdapter

class SearchFragment : Fragment() {

    private lateinit var categoryAdaptor: CategoryRecyclerAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isInternetAvailable(view.context)) {
            // Initialize the ViewModel with a factory
            val viewModelFactory = SearchViewModelFactory(
                RepositoryImplement(
                    LocalDataBaseImplement(this.requireContext()),
                    MealLocalDsImplement(this.requireContext()),
                    FavouritesLocalDsImplement(this.requireContext()),
                    APIClient
                )
            )
            val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

            // Initialize the RecyclerView and Adapter with an empty list
            val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.cat_recyclerview)
            categoryAdaptor = CategoryRecyclerAdaptor(emptyList())
            categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.adapter = categoryAdaptor
            viewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
                categoryAdaptor.updateData(categoryList ?: emptyList())
            }


            // Observe error messages from the ViewModel
            viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            // Fetch the categories from the ViewModel
            viewModel.getALLCategories()

        } else {
            Log.d("SearchFragment", "Error: Network not available")
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
        }
    }
}