package com.example.githubapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val username : String,
    val avatarUrl: String,
    val followersUrl: String,
    val followingUrl: String
) : Parcelable