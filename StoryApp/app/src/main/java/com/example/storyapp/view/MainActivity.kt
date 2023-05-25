package com.example.storyapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.ListAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.Story
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.upload.UploadActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreference: UserPreference
    private lateinit var viewModel: MainViewModel
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        userPreference = UserPreference(this)
        if (userPreference.getDataLogin().token.isEmpty()){
            finish()
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        showExistingPreference()

        val manager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = manager
        binding.rvStory.setHasFixedSize(true)
        viewModel.listUser.observe(this){
            setUserStory(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        val logoutBtn = menu?.findItem(R.id.logout)
        val cameraBtn = menu?.findItem(R.id.upStory)

        logoutBtn?.setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Anda yakin ingin keluar?")
            builder.setPositiveButton("Yes") {_,_->
                userPreference.logout()
                finish()
            }
            builder.setNegativeButton("Cancel", null)
            val dialog = builder.create()
            dialog.show()
            true
        }

        cameraBtn?.setOnMenuItemClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
            true
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun showExistingPreference(){
        userModel = userPreference.getDataLogin()
        populateView(userModel)
    }

    private fun populateView(userModel: UserModel){
        supportActionBar?.title = userModel.name
        viewModel.fetchStory(userModel.token)
    }

    private fun setUserStory(noteStory : List<Story>) {
        val sortedList = noteStory.sortedBy {
            it.createdAt
        }.reversed()
        if (noteStory.isNotEmpty()){
            val adapter = ListAdapter(sortedList)
            binding.rvStory.adapter = adapter
        }else{
            Toast.makeText(this, "Gagal mengambil data dari API", Toast.LENGTH_LONG).show()
        }
    }
}