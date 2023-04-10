package com.example.githubapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.database.UserGithub
import com.example.githubapp.databinding.UserDetailBinding
import com.example.githubapp.favorite.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: UserDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object{
        const val USER_KEY = "key_data"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory : ViewModelProvider.Factory = ViewModelProvider.NewInstanceFactory()
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        val username = intent.getStringExtra(USER_KEY)
        val bundle = Bundle()
        bundle.putString(USER_KEY, username)


        val loginUser = intent.getStringExtra(USER_KEY)
        binding.tvName.text = loginUser

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = loginUser.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tab_detail)
        TabLayoutMediator(tabs, viewPager) {tabs, position ->

            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        viewModel.detailUser.observe(this){
                detailUser -> setDetailUser(detailUser)
                showLoading(false)
        }

        if (username != null){
            showLoading(true)
            viewModel.getUserDetail(username)
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setDetailUser(Data: DetailUserResponse) {
        Glide.with(this@DetailUserActivity)
            .load(Data.avatarUrl)
            .into(binding.ivUserdetail)
        binding.tvName.text = Data.login
        binding.tvUsername.text = Data.name
        binding.tvFollowers.text = Data.followers.toString()
        binding.tvFollowing.text = Data.following.toString()

        binding.btnFav?.setOnClickListener {
            val userRoom = UserGithub(Data.login, Data.name, Data.followers.toString(), Data.following.toString())
            viewModel.insertData(userRoom)
            Toast.makeText(this,"User Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}


