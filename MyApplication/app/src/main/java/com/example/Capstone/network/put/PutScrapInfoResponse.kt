package com.example.Capstone.network.put

import com.example.Capstone.network.get.GetSpecificScrapResponse


data class PutScrapInfoResponse(
    val scrap : GetSpecificScrapResponse
)