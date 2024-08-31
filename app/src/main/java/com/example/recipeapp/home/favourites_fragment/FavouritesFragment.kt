//package com.example.recipeapp.home.favourites_fragment
//
//import SpaceItemDecoration
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.recipeapp.R
//import com.example.recipeapp.appstorage.RepositoryImplement
//import com.example.recipeapp.database.favourites.FavouritesLocalDsImplement
//import com.example.recipeapp.database.meal.MealLocalDsImplement
//import com.example.recipeapp.database.user.LocalDataBaseImplement
//import com.example.recipeapp.home.adapters.FavouritesAdapter
//import com.example.recipeapp.home.home_fragment.HomeViewFactory
//import com.example.recipeapp.home.home_fragment.HomeViewModel
//import com.example.recipeapp.network.APIClient
//import com.example.task2.PopularAdapter
//
//class FavouritesFragment : Fragment() {
//    private lateinit var favAdapter: FavouritesAdapter
//    private lateinit var sharedPreferences: SharedPreferences
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favourites, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val viewModelFactory = FavouritesViewModelFactory(
//            RepositoryImplement(
//                LocalDataBaseImplement(requireContext()),
//                MealLocalDsImplement(requireContext()),
//                FavouritesLocalDsImplement(requireContext()),
//                APIClient
//            )
//        )
//        val viewModel = ViewModelProvider(this, viewModelFactory).get(FavouritesViewModel::class.java)
//        sharedPreferences = requireActivity().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
//        val userId = sharedPreferences.getInt("id", -1)
//        Log.d("asd", userId.toString())
//
//        val mealsRv = view.findViewById<RecyclerView>(R.id.fav_rv)
//        mealsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        mealsRv.addItemDecoration(SpaceItemDecoration(20))
//
//        favAdapter = FavouritesAdapter(viewModel.favourites.value ?: emptyList()) {
//            viewModel.deleteFavourite(it, userId)
//        }
//        mealsRv.adapter = favAdapter
//
//
//        viewModel.favourites.observe(viewLifecycleOwner) { favorites ->
//            favAdapter.updateData(viewModel.favourites.value ?: emptyList())
//        }
//
//
//        viewModel.getFavourites(userId)
//    }
//
//}