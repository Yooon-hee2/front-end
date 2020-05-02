package com.example.Capstone.network

import com.example.Capstone.network.get.GetAllScrapListResponse
import com.example.Capstone.network.get.GetFolderScrapListResponse
import com.example.Capstone.network.post.PostLoginResponse
import com.example.Capstone.network.post.PostScrapResponse
import com.example.Capstone.network.post.PostSignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface NetworkService {

    //login & signup -----------------------------------------------

    //login
    @POST("/memmem_app/auth/login/")
    fun postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostLoginResponse>

    //sign up
    @POST("/memmem_app/auth/register/")
    fun postSignupResponse(
    @Header("Content-Type") content_type: String,
    @Body() body: JsonObject
    ): Call<PostSignUpResponse>

    //send url for crawling
    @POST("/user/nickname/")
    fun postScrapResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostScrapResponse>


    //scrap information -----------------------------------------------
    //scrap list for all folders
    @GET("/memmem_app/users/{id}/listall")
    fun getAllScrapListResponse(
        @Path("id") id : Int
    ): Call<ArrayList<GetAllScrapListResponse>>

//    //scrap list for specific folders
//    @GET("/users/{id}/{folder_id}")
//    fun getFolderScrapListResponse(
//        @Path("id") id : Int,
//        @Path("folder_id") folder_id : Int
//    ): Call<ArrayList<GetFolderScrapListResponse>>
}
