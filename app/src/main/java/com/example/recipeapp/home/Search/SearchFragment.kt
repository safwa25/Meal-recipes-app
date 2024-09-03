package com.example.recipeapp.home.Search

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.R
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
import com.example.recipeapp.database.meal.MealLocalDsImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.dto.Category
import com.example.recipeapp.dto.Meal
import com.example.recipeapp.network.APIClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

    class SearchFragment : Fragment(), OnCategoryClickListener {

    private lateinit var categoryAdaptor: CategorySearchAdapter
    private lateinit var searchAdaptor: SearchFragmentAdapter
    private lateinit var searchViewvar: androidx.appcompat.widget.SearchView
    private lateinit var StartAnimationView: ConstraintLayout
    private lateinit var animationtext: TextView
    private lateinit var animation: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("habiba", "Fragment done")
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

        val searchViewModelFactory = SearchViewModelFactory(
            RepositoryImplement(
                LocalDataBaseImplement(this.requireContext()),
                MealLocalDsImplement(this.requireContext()),
                FavouritesLocalDsImplement(this.requireContext()),
                APIClient
            )
        )

        val searchViewModel = ViewModelProvider(this, searchViewModelFactory).get(SearchViewModel::class.java)

        // Initialize the Category RecyclerView
        val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.cat_recyclerview)
        categoryAdaptor = CategorySearchAdapter(emptyList(), this)
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.adapter = categoryAdaptor

        // Initialize the Search RecyclerView
        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.search_recycler)
        searchAdaptor = SearchFragmentAdapter(emptyList(),{meal->onRecipeClick(meal)})
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.adapter = searchAdaptor

        // Set up the SearchView
        searchViewvar = view.findViewById(R.id.search_textbox)
        setupSearchView(searchViewModel, searchRecyclerView)

        // Observers for ViewModel
        searchViewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            categoryAdaptor.updateData(categoryList ?: emptyList())
        }

        searchViewModel.SearchList.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults != null) {
                if (searchResults.isEmpty()) {
                    // Show "No Recipe Found" animation if no results
                    showNoRecipeAnimation(animationtext, animation)
                    searchRecyclerView.visibility = View.GONE
                } else {
                    // Show search results
                    searchAdaptor.updateData(searchResults)
                    StartAnimationView.visibility = View.GONE
                    searchRecyclerView.visibility = View.VISIBLE
                }
            } else {
                searchAdaptor.updateData(emptyList())
            }
        }

        searchViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Delay before showing loading animation to allow start animation to play
        CoroutineScope(Dispatchers.Main).launch {
            showLoadingAnimation(animationtext, animation)

            delay(1000)  // Adjust delay as needed
            searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    // Show loading animation after start animation has played
                    showStartAnimation(animationtext, animation)
                    searchRecyclerView.visibility = View.GONE
                }
            }
        }

        searchViewModel.getALLCategories()

        fun check(MealId:String,UserId:Int):Boolean
        {
            searchViewModel.checing(MealId,UserId)
            searchViewModel.checking.observe(viewLifecycleOwner){
                found->

//                return@observe found
            }
        }
    }




    fun onRecipeClick(meal:Meal)
    {
        val action = SearchFragmentDirections.actionSearchFragmentToRecipeDetails(meal)
        findNavController().navigate(action)
    }

    private fun setupSearchView(viewModel: SearchViewModel, searchRecyclerView: RecyclerView) {
        searchViewvar.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private val handler = Handler(Looper.getMainLooper())
            private val runnable: Runnable = Runnable {
                searchViewvar.query?.let { query ->
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
                if (newText.isNullOrEmpty()) {
                    // Handle when the search text is cleared by pressing X or by deleting with the keyboard
                    showStartAnimation(animationtext, animation)
                    StartAnimationView.visibility = View.VISIBLE
                    searchRecyclerView.visibility = View.GONE
                } else {
                    // Handle text input as usual
                    if (isInternetAvailable(requireContext())) {
                        StartAnimationView.visibility = View.GONE
                        searchRecyclerView.visibility = View.VISIBLE

                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, 300) // Adjust delay as needed
                    } else {
                        showNoInternetAnimation(animationtext, animation)
                        searchRecyclerView.visibility = View.GONE
                    }
                }
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
        animation.setAnimation(R.raw.no_internet_anim)
        animation.playAnimation()
    }

    fun showLoadingAnimation(animationtext: TextView, animation: LottieAnimationView) {
        animationtext.text = "Loading, please wait"
        animation.setAnimation(R.raw.loading_anim)
        animation.playAnimation()
    }

    override fun onCategoryClick(category: Category) {
        Log.d("habiba", "Title: ${category.strCategory}, ID: ${category.idCategory}")

        val bundle = Bundle().apply {
            putString("title", category.strCategory)
            putString("image", category.strCategoryThumb)
        }

        // Navigate to the new fragment with the bundle
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_searchFragment_to_categoryFragment, bundle)
        }
    }
}
