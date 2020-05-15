package com.example.Capstone.model

import com.example.Capstone.network.data.MemoData
import com.example.Capstone.network.data.TagData

data class Feed (
    var id : Int,
    var src : String,
    var title : String,
    var thumbnail : String?,
    var domain : String,
    val memos : ArrayList<MemoData>,
    val tags : ArrayList<TagData>
)