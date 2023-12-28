package com.example.authviacode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val liveDataUser = MutableLiveData<User>()
}