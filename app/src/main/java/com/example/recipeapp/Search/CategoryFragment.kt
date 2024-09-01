//import CategorySpaceItemDecoration
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.recipeapp.R
//import com.example.recipeapp.Search.Each_cat_item_RecyclerAdaptor
//import com.example.recipeapp.Search.SearchViewModel
//import com.example.recipeapp.Search.SearchViewModelFactory
//import com.example.recipeapp.appstorage.RepositoryImplement
//import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
//import com.example.recipeapp.database.meal.MealLocalDsImplement
//import com.example.recipeapp.database.user.LocalDataBaseImplement
//import com.example.recipeapp.network.APIClient
//
//class CategoryFragment : Fragment() {
//
//    private lateinit var categoryitemsAdaptor: Each_cat_item_RecyclerAdaptor
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//
//    ): View? {
//        Log.d("CategoryFragment", "onCreateView called")
//
//        return inflater.inflate(R.layout.fragment_category, container, false)
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d("CategoryFragment", "onViewCreated called")
//    }
//}
//
//////        // Retrieve the data from the bundle
//////        val categoryTitle = arguments?.getString("title") ?: ""
//////        val categoryImage = arguments?.getString("image")
////
////        // Find the TextView and ImageView by ID
////        val titleTextView = view.findViewById<TextView>(R.id.category_title)
////        val imageView = view.findViewById<ImageView>(R.id.category_image)
//////
//////        // Set the title and image
//////        titleTextView.text = categoryTitle
////        titleTextView.text = "seafood"
//////        categoryImage?.let {
//////            Log.d("CategoryFragment", "Loading image with URL: $it")
//////            Glide.with(this).load(it).into(imageView)
//////        }
////          Log.d("asd_habiba","fragment built")
////        // Initialize the Category RecyclerView
////        val each_cat_item_recycler = view.findViewById<RecyclerView>(R.id.cat_item_recycler_view)
////        categoryitemsAdaptor = Each_cat_item_RecyclerAdaptor(emptyList())
////        each_cat_item_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
////        each_cat_item_recycler.adapter = categoryitemsAdaptor
////        each_cat_item_recycler.addItemDecoration(CategorySpaceItemDecoration(1))
////        Log.d("CategoryFragment", "Recycler view set up with adapter")
////
////        // Observe LiveData from ViewModel
////        val viewModelFactory = SearchViewModelFactory(
////            RepositoryImplement(
////                LocalDataBaseImplement(requireContext()),
////                MealLocalDsImplement(requireContext()),
////                FavouritesLocalDsImplement(requireContext()),
////                APIClient
////            )
////        )
////        val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
////        viewModel.getMealsbyCategory("SeaFood")
////
////
////        viewModel.Categorttype.observe(viewLifecycleOwner) { categorttype ->
////            Log.d("CategoryFragment", "Observer triggered with ${categorttype?.size} items")
////            categorttype?.let {
////                categoryitemsAdaptor.updateData(it)
////                Log.d("CategoryFragment", "Adapter data updated with ${it.size} items")
////            }
////        }
////
//////        // Fetch meals by category
//////        if (categoryTitle.isNotEmpty()) {
//////            Log.d("CategoryFragment", "Fetching meals for category: $categoryTitle")
//////            viewModel.getMealsbyCategory(categoryTitle)
//////        } else {
//////            Log.e("CategoryFragment", "Category title is empty")
//////        }
////    }
////
////    private fun isInternetAvailable(context: Context): Boolean {
////        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
////        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            val network = connectivityManager.activeNetwork ?: return false
////            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
////            activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
////                    activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
////        } else {
////            @Suppress("DEPRECATION")
////            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
////            @Suppress("DEPRECATION")
////            networkInfo.isConnected
////        }
////   }
////}
