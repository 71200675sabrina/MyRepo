package com.example.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch


class LoginViewModel (private val pref: UserPreference) : ViewModel(){

    fun getDataLogin(): LiveData<UserModel>{
        return MutableLiveData<UserModel>().apply {
            value = pref.getDataLogin()
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}