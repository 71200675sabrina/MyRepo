package com.example.githubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubapp.database.UserGithub

class UserGithubDiffCallback (
    private val newList: List<UserGithub>,
    private val oldList: List<UserGithub>,
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].username == newList[newItemPosition].username

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser.username == newUser.username && oldUser.id == newUser.id
    }
}