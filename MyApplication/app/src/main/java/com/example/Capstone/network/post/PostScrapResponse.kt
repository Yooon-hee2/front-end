package com.example.Capstone.network.post

import com.example.Capstone.network.get.GetAllFolderScrapListResponse

data class PostScrapResponse(
    val message : String,
    val status : String,
    val scrap : GetAllFolderScrapListResponse?
)