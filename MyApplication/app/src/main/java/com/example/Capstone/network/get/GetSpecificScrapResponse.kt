package com.example.Capstone.network.get

import com.example.Capstone.network.data.MemoData
import com.example.Capstone.network.data.TagData

data class GetSpecificScrapResponse(
    val scrap_id : Int,
    val folder : Int,
    val title : String,
    val url : String,
    val date : String,
    val thumbnail : String,
    val domain : String,
    val memos : ArrayList<MemoData>,
    val tags : ArrayList<TagData>
)