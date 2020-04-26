package com.example.Capstone.network

import com.example.Capstone.network.get.PostSignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface NetworkService {

//    //----------로그인/회원가입---------
//    //로그인
//    @POST("/user/signin")
//    fun postLoginResponse(
//        @Header("Content-Type") content_type: String,
//        @Body() body: JsonObject
//    ): Call<PostLoginResponse>
//
    //회원가입
    @POST("/memmem_app/auth/register")
    fun postSignupResponse(
    @Header("Content-Type") content_type: String,
    @Body() body: JsonObject
    ): Call<PostSignUpResponse>

//    //닉네임 중복 검사
//    @POST("/user/nickname")
//    fun postNicknameCheckResponse(
//        @Header("Content-Type") content_type: String,
//        @Body() body: JsonObject
//    ): Call<PostNicknameCheckResponse>
}