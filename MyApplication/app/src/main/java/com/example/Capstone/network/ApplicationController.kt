package com.example.Capstone.network

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.Capstone.R
import com.example.Capstone.background.LocationWorker
import com.example.Capstone.background.TimeWorker
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApplicationController : Application(){
    private val baseURL = "https://27c750d5.ngrok.io" //ngrok켤때마다 바꿀것
    lateinit var networkService: NetworkService

    companion object{
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetWork()
        makeNotificationChannel()
        activateWorker()
    }

    var okHttpClient = OkHttpClient.Builder() //set timeout
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun buildNetWork(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
//            .build()

        networkService = retrofit.create(NetworkService::class.java)
    }

    private fun makeNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("멤멤", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun activateWorker(){
        val workRequestAboutLocation = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES).build()
        val workRequestAboutTime = PeriodicWorkRequestBuilder<TimeWorker>(2, TimeUnit.HOURS).build()
        val workManager = WorkManager.getInstance()
        workManager?.enqueue(workRequestAboutLocation)
        workManager?.enqueue(workRequestAboutTime)
    }
}