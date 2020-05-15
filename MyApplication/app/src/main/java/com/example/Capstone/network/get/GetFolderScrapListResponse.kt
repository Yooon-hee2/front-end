package com.example.Capstone.network.get

import com.example.Capstone.model.Feed

data class GetFolderScrapListResponse (
    val folder_id : Int,
    val folder_name : String,
    val scraps : ArrayList<GetAllFolderScrapListResponse>?
)
