package com.example.githubapp.favorite

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapp.database.UserGithub
import com.example.githubapp.databinding.ActivityFavoriteBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.DetailUserActivity
import com.example.githubapp.ViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var listUsers : List<UserGithub>
    private val favoriteViewModel by viewModels<FavoriteViewModel>() { ViewModelFactory.getInstance(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationAdapter()
        setData()

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserGithub) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.USER_KEY,data.username)
                startActivity(intent)
            }
        })
        }

        private fun initializationAdapter(){
            binding.rvFavorite.setHasFixedSize(true)
            binding.rvFavorite.layoutManager = LinearLayoutManager(this)
            adapter = FavoriteAdapter()
            binding.rvFavorite.adapter = adapter

        }

        private fun setData(){
            favoriteViewModel.getUser().observe(this){listUsers ->
                adapter.setListUsers(listUsers)
                binding.rvLoad.visibility = View.GONE
            }
        }
}