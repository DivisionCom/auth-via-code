package com.example.authviacode

data class User(
    val phone: String,
    val code: String? = null
)
