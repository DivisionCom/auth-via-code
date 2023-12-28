package com.example.authviacode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.authviacode.data.remote.User
import com.example.authviacode.data.remote.responses.Token

class MainViewModel: ViewModel() {
    val liveDataUser = MutableLiveData<User>()
    val liveDataToken = MutableLiveData<Token>()
}