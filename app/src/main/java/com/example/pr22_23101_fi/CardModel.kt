package com.example.pr22_23101_fi

data class Card(
    val id: Int,
    val imageId: Int,
    val imageName: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)