package com.example.githubapp.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.githubapp.UserRepository
import com.example.githubapp.database.UserGithub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    fun getUser(): LiveData<List<UserGithub>> = userRepository.getAllFavoriteData()

    fun getUserByUsername(username: String) : LiveData<UserGithub> = userRepository.getDataByUsername(username)

    fun insertUser(user: UserGithub){
        userRepository.insert(user)
    }

    fun deleteUser(user: UserGithub){
        userRepository.delete(user)
    }


}