package com.example.Capstone.network

import com.example.Capstone.network.delete.DeleteFolderResponse
import com.example.Capstone.network.get.*
import com.example.Capstone.network.post.PostFolderResponse
import com.example.Capstone.network.post.PostLoginResponse
import com.example.Capstone.network.post.PostScrapResponse
import com.example.Capstone.network.post.PostSignUpResponse
import com.example.Capstone.network.put.PutScrapInfoResponse
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



    //get all folder list
    @GET("/memmem_app/users/{id}/folders/")
    fun getAllFolderListResponse(
        @Path("id") id : Int
    ): Call<ArrayList<GetAllFolderListResponse>>

    //create new folder
    @POST("/memmem_app/addfolder/")
    fun postFolderResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostFolderResponse>

    //delete folder
    @DELETE("/memmem_app/users/{user_id}/folders/{folder_id}/")
    fun deleteFolderResponse(
        @Header("Content-Type") content_type: String,
        @Path("user_id") user_id : Int,
        @Path("folder_id") folder_id : Int
    ): Call<DeleteFolderResponse>

    //scrap information -----------------------------------------------
    //scrap list for all folders
    @GET("/memmem_app/users/{id}/listall/")
    fun getAllScrapListResponse(
        @Path("id") id : Int
    ): Call<ArrayList<GetAllScrapListResponse>>

    //scrap list for specific folders
    @GET("/memmem_app/users/{id}/folders/{folder_key}/listall/")
    fun getFolderScrapListResponse(
        @Path("id") id : Int,
        @Path("folder_key") folder_key : Int
    ): Call<ArrayList<GetFolderScrapListResponse>>

    //show scrap detail
    @GET("/memmem_app/scrap/{id}/")
    fun getSpecificScrapResponse(
        @Path("id") id : Int
    ): Call<GetSpecificScrapResponse>

    //send url for crawling
    @POST("/memmem_app/addscrap/")
    fun postScrapResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostScrapResponse>

    //send modified scrap information
    @PUT("/memmem_app/updatescrap/{id}/")
    fun putScrapInfoResponse(
        @Header("Content-Type") content_type: String,
        @Path("id") id : Int,
        @Body() body: JsonObject
    ): Call<PutScrapInfoResponse>

}

