package com.example.githubapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserGithub)

    @Query("DELETE FROM user WHERE username = :username")
    fun delete(user: UserGithub)

    @Query("SELECT * from user ORDER BY id ASC")
    fun getAllFavoriteData(): LiveData<List<UserGithub>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<UserGithub>
}