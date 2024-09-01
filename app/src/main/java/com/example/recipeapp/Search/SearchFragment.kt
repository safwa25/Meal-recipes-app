package com.example.recipeapp.Search

import CategorySpaceItemDecoration
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.dto.Category
import com.example.recipeapp.network.APIClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(){

    private lateinit var categoryAdaptor: CategoryRecyclerAdaptor
    private lateinit var searchAdaptor: SearchRecycleAdapter
    private lateinit var searchViewvar: androidx.appcompat.widget.SearchView
    private lateinit var StartAnimationView: ConstraintLayout
    private lateinit var animationtext: TextView
    private lateinit var animation: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize animation views
        StartAnimationView = view.findViewById(R.id.Start_Animation)
        animationtext = view.findViewById(R.id.searchtext)
        animation = view.findViewById(R.id.lottieAnimationView)

        // Show start animation when fragment is first created
        StartAnimationView.visibility = View.VISIBLE
        showStartAnimation(animationtext, animation)

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
            val viewModel =
                ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

            // Initialize the Category RecyclerView
            val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.cat_recyclerview)
            categoryAdaptor = CategoryRecyclerAdaptor(emptyList())
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.adapter = categoryAdaptor
            categoryRecyclerView.addItemDecoration(CategorySpaceItemDecoration(1))

            // Initialize the Search RecyclerView
            val searchRecyclerView = view.findViewById<RecyclerView>(R.id.search_recycler)
            searchAdaptor = SearchRecycleAdapter(emptyList())
            searchRecyclerView.layoutManager = LinearLayoutManager(context)
            searchRecyclerView.adapter = searchAdaptor

            // Set up the SearchView
            searchViewvar = view.findViewById(R.id.search_textbox)
            setupSearchView(viewModel)

            // Observers for ViewModel
            viewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
                categoryAdaptor.updateData(categoryList ?: emptyList())
            }

            viewModel.SearchList.observe(viewLifecycleOwner) { searchResults ->
                if (searchResults != null) {
                    if (searchResults.isEmpty()) {
                        // Show "No Recipe Found" animation if no results
                        showNoRecipeAnimation(animationtext, animation)
                        searchRecyclerView.visibility = View.GONE
                    } else {
                        // Show search results
                        searchAdaptor.updateData(searchResults)
                        showStartAnimation(animationtext, animation) // Resets to initial animation
                        searchRecyclerView.visibility = View.VISIBLE
                    }
                    Log.d("SearchFragment", "Search results updated: $searchResults")
                } else {
                    searchAdaptor.updateData(emptyList())
                }
            }

            viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoadingAnimation(animationtext, animation)
                    searchRecyclerView.visibility = View.GONE
                }
            }

            viewModel.getALLCategories()



        } else {
            Log.d("SearchFragment", "Network not available")
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            showNoInternetAnimation(animationtext, animation)
            // Launch a coroutine to check connectivity continuously
            CoroutineScope(Dispatchers.Main).launch {
                while (!isInternetAvailable(view.context)) {
                    delay(2000L) // Delay for 2 seconds before checking again
                }
                // Once the network is back online, handle the network change
                handleNetworkChange(true)
            }

        }

    }


    private fun setupSearchView(viewModel: SearchViewModel) {
        searchViewvar?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private val handler = Handler(Looper.getMainLooper())
            private val runnable: Runnable = Runnable {
                searchViewvar?.query?.let { query ->
                    if (query.isNotBlank()) {
                        Log.d("SearchFragment", "Query submitted: $query")
                        viewModel.getMealByName(query.toString())
                    }
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        Log.d("SearchFragment", "Query submitted: $it")
                        viewModel.getMealByName(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Hide the start animation when typing begins
                StartAnimationView.visibility = View.GONE

                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 300) // Adjust delay as needed
                return true
            }
        })
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

    fun showStartAnimation(animationtext: TextView, animation: LottieAnimationView) {
        animationtext.text = "Looking for Tasty meal?"
        animation.setAnimation(R.raw.searching_anim)
        animation.playAnimation()
    }

    private fun showNoRecipeAnimation(animationtext: TextView, animation: LottieAnimationView) {
        StartAnimationView.visibility = View.VISIBLE  // Make sure the animation view is visible
        animationtext.text = "Sorry...No such recipe"
        animation.setAnimation(R.raw.not_found)
        animation.playAnimation()

    }


    fun showNoInternetAnimation(animationtext: TextView, animation: LottieAnimationView) {
        animationtext.text = "No Internet Connection"
        animation.setAnimation(R.raw.no_internet)
        animation.playAnimation()
    }

    fun showLoadingAnimation(animationtext: TextView, animation: LottieAnimationView) {
        animationtext.text = "Loading, please wait"
        animation.setAnimation(R.raw.loading)
        animation.playAnimation()
    }

    private fun handleNetworkChange(isOnline: Boolean) {
        if (isOnline) {
            Toast.makeText(context, "Network reconnected!", Toast.LENGTH_SHORT).show()

            // Hide the "No Internet" animation
            StartAnimationView.visibility = View.GONE

//            // Reinitialize ViewModel or any necessary data
//            val viewModelFactory = SearchViewModelFactory(
//                RepositoryImplement(
//                    LocalDataBaseImplement(this.requireContext()),
//                    MealLocalDsImplement(this.requireContext()),
//                    FavouritesLocalDsImplement(this.requireContext()),
//                    APIClient
//                )
//            )
//            val viewModel =
//                ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
//
//            setupSearchView(viewModel)
//            viewModel.getALLCategories()

            // Optionally, trigger any data refresh or other actions now that you're back online
        }
    }

//    override fun onCategoryClick(category: Category) {
//        val bundle = Bundle().apply {
//            putString("title", category.strCategory)
//            putString("image", category.strCategoryThumb)
//            putString("id",category.idCategory)
//        }
//        // Navigate to the new fragment with the bundle
//        view?.let {
//            Navigation.findNavController(it)
//                .navigate(R.id.action_searchFragment_to_categoryFragment)
//        }
//    }
}

