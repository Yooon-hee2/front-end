package com.example.Capstone.network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationController : Application(){
    private val baseURL = "http://127.0.0.1:8000"
    //주소 뜨기전까지 주석 살리지 말것
    lateinit var networkService: NetworkService

    companion object{
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetWork()
    }

    private fun buildNetWork(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create()).build()
//            .build()

        networkService = retrofit.create(NetworkService::class.java)
    }
}