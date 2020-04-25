package com.example.Capstone.model

data class Feed (
    var id : Int,
    var src : String,
    var title : String,
    var thumbnail : String,
    var hashtag: ArrayList<String>?
)