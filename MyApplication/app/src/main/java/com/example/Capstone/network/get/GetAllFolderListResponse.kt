package com.example.Capstone.network.get

import com.example.Capstone.network.data.FolderData

data class GetAllFolderListResponse (
    val id : Int,
    val username : String,
    val folders : ArrayList<FolderData>
)