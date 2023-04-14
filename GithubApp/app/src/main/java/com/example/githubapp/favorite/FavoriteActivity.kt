package com.example.githubapp.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapp.database.UserGithub
import com.example.githubapp.databinding.ActivityFavoriteBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.UserAdapter
import com.example.githubapp.Users
import com.example.githubapp.ViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>() { ViewModelFactory.getInstance(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = manager
        binding.rvFavorite.setHasFixedSize(true)

        favoriteViewModel.getUser().observe(this){
            setUserData(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(listUser : List<UserGithub>){
        val userGithub = ArrayList<Users>()
        listUser.map {
            val user = Users(username = it.username.toString(), avatarUrl = it.avatarUrl.toString(), followersUrl = it.followersUrl.toString(), followingUrl = it.followingUrl.toString())
            userGithub.add(user)
        }
        if (userGithub.isNotEmpty()){
            val adapter = UserAdapter(userGithub)
            binding.rvFavorite.adapter = adapter
        }else {
            val adapter = UserAdapter(userGithub)
            binding.rvFavorite.adapter = adapter
        }
    }

}