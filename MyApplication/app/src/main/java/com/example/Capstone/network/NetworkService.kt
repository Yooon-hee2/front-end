package com.example.Capstone.network

import com.example.Capstone.network.delete.DeleteFolderResponse
import com.example.Capstone.network.delete.DeleteSpecificScrapResponse
import com.example.Capstone.network.get.*
import com.example.Capstone.network.post.*
import com.example.Capstone.network.put.PutRecrawlResponse
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

    //folder information -----------------------------------------------

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

    //delete scrap detail
    @DELETE("/memmem_app/scrap/{scrap_id}/")
    fun deleteSpecificScrapResponse(
        @Header("Content-Type") content_type: String,
        @Path("scrap_id") scrap_id : Int
    ): Call<DeleteSpecificScrapResponse>

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


    //notification---------------------------------------------------------------------

    //location alarm
    @POST("/memmem_app/location/user/{id}/")
    fun postLocationAlarmResponse(
        @Header("Content-Type") content_type: String,
        @Path("id") id : Int,
        @Body() body: JsonObject
    ): Call<PostLocationAlarmResponse>

    //check location alarm list is empty
    @POST("/memmem_app/findlocation/user/{id}/")
    fun postLocationAlarmisNullResponse(
        @Header("Content-Type") content_type: String,
        @Path("id") id : Int,
        @Body() body: JsonObject
    ): Call<PostLocationAlarmisNullResponse>

    //time alarm
    @GET("/memmem_app/food/user/{id}/")
    fun getTimeAlarmResponse(
        @Header("Content-Type") content_type: String,
        @Path("id") id : Int
    ): Call<GetTimeAlarmResponse>

    //sharing storage-------------------------------------------

    //check member is existed
    @POST("/memmem_app/search/")
    fun postMemberisExistedResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostLocationAlarmisNullResponse>

    //create new sharing storage
    @POST("/memmem_app/addsharing/")
    fun postNewStorageResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<PostLocationAlarmisNullResponse>

    //get sharing storage list
    @GET("/memmem_app/users/{id}/sharinglist/")
    fun getAllStorageListResponse(
        @Header("Content-Type") content_type: String,
        @Path("id") id : Int
    ): Call<ArrayList<GetAllStorageListResponse>>
}

