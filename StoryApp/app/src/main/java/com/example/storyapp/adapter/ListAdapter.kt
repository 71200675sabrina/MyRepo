package com.example.storyapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemDetailBinding
import com.example.storyapp.model.Story
import com.example.storyapp.view.StoryDetailActivity

class ListAdapter (private  val listStory: List<Story>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {
        holder.progressBar.visibility = View.VISIBLE
        val storyItem = listStory[position]
        val imageUrl = storyItem.photoUrl
        Glide.with(holder.itemView.context).load(imageUrl).into(holder.binding.ivstory)
        holder.binding.tvstory.text = storyItem.name
        holder.binding.tvDate.text = storyItem.createdAt
        holder.binding.loadstory.visibility = View.GONE

        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.binding.root.context, StoryDetailActivity::class.java)
            intent.putExtra(StoryDetailActivity.STORY_KEY, listStory[holder.adapterPosition])
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(var binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val progressBar : ProgressBar = binding.loadstory
    }
}