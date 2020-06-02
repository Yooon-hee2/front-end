package com.example.Capstone.network.post

import com.example.Capstone.network.data.FolderData

data class PostFolderResponse (
    val message : String,
    val status : String,
    val folders : ArrayList<FolderData>
)