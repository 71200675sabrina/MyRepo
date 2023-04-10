package com.example.githubapp

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.database.UserDao
import com.example.githubapp.database.UserGithub
import com.example.githubapp.database.UserGithubRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val userDao: UserDao

    init {
        val database = UserGithubRoomDatabase.getDatabase(application)
        userDao = database.userDao()
    }

    fun getAllFavoriteData(): LiveData<List<UserGithub>> = userDao.getAllFavoriteData()

    fun insert(user: UserGithub){
        executorService.execute { userDao.insert(user) }
    }

    fun delete(username: String){
        executorService.execute { userDao.delete(username) }
    }

    fun getDataByUsername(username: String): LiveData<UserGithub> = userDao.getDataByUsername(username)
}