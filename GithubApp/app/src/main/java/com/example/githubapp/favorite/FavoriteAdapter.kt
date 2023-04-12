package com.example.githubapp.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.database.UserGithub
import com.bumptech.glide.request.RequestOptions
import com.example.githubapp.databinding.ItemUserBinding
import com.example.githubapp.helper.UserGithubDiffCallback


class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val listUsers = ArrayList<UserGithub>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setListUsers(listUsers: List<UserGithub>) {
        val userDiffcallback = UserGithubDiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(userDiffcallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(users: UserGithub) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(users.avatarUrl)
                    .apply(RequestOptions().override(60, 60))
                    .into(ivUser)
                tvUser.text = users.username

                itemView.setOnClickListener{ onItemClickCallback?.onItemClicked(users) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserGithub)
    }
}