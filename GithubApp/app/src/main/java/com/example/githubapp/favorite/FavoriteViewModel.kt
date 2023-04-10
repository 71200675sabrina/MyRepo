package com.example.githubapp.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.UserRepository
import com.example.githubapp.database.UserGithub

class FavoriteViewModel(application: Application): ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    fun getUser(): LiveData<List<UserGithub>> = userRepository.getAllFavoriteData()
    fun deleteUser(username: String){
        userRepository.delete(username)
    }

}