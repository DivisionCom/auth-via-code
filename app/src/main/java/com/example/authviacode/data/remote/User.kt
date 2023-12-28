package com.example.authviacode.data.remote

data class User(
    val phone: String,
    val code: String? = null
)
