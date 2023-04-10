package com.example.githubapp.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapp.database.UserGithub
import com.example.githubapp.databinding.ActivityFavoriteBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.ViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var listUser: List<UserGithub>
    private lateinit var binding: ActivityFavoriteBinding

    val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
    val favoriteViewModel: FavoriteViewModel by  viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationAdapter()
        setData()
    }

    private fun setData() {
        favoriteViewModel.getUser().observe(this){
            if (it != null){
                setLoading()
                listUser = it
                adapter.setListUsers(it)
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listUser[viewHolder.adapterPosition].username?.let { favoriteViewModel.deleteUser(it)}
                }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFavorite)
    }

    private fun initializationAdapter(){
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter()
        binding.rvFavorite.adapter = adapter
    }

    private fun setLoading(){
        binding.rvLoad.visibility = View.GONE
        binding.rvFavorite.visibility = View.VISIBLE
    }

}