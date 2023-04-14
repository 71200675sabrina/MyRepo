package com.example.githubapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserGithub::class], version = 2)
abstract class UserGithubRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserGithubRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserGithubRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserGithubRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserGithubRoomDatabase::class.java, "user_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

            }
            return INSTANCE as UserGithubRoomDatabase
        }
    }
}