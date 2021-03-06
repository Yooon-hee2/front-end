package com.example.Capstone.network.get

import com.example.Capstone.model.Feed
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
) {
    fun toFeedDetail(): Feed {
        return Feed(
            id = scrap_id, src = url, title = title, thumbnail = thumbnail, domain = domain, memos = memos, tags = tags
        )
    }
}