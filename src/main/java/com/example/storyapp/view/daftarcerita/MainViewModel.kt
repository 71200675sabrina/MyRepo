package com.example.storyapp.view.daftarcerita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel (private val pref: UserPreference): ViewModel() {
    private val dataLogin: MutableLiveData<UserModel> = MutableLiveData()
    init {
        dataLogin.value = pref.getDataLogin()
    }

    fun getDataLogin(): LiveData<UserModel>{
        return dataLogin
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
            dataLogin.value = pref.getDataLogin()
        }
    }
}