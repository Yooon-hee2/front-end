package com.example.Capstone.background


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.Capstone.ImageUtils.getCircularBitmap
import com.example.Capstone.R
import com.example.Capstone.activities.NotificationListActivity
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetTimeAlarmResponse
import com.example.Capstone.network.post.PostLocationAlarmisNullResponse
import com.example.Capstone.network.post.PostSignUpResponse
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {

    val NOTICATION_ID = 227
    private val LOCATION_REQUEST_CODE = 1000
    private var fusedLocationProviderClient : FusedLocationProviderClient? = null
    private var locationCallback : LocationCallback? = null

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onReceive(context: Context, intent: Intent) {

        val code: Int = intent.getIntExtra("id", 1)

        Log.d("requestcode", code.toString())

        val current = LocalDateTime.now()

        val mealTime = arrayListOf("08", "09", "12", "11", "18", "19")
        val calendar = Calendar.getInstance()

        val date = current.format(DateTimeFormatter.ISO_DATE)
        val time = current.format(DateTimeFormatter.ISO_TIME)

        if(SharedPreferenceController.getUserNoti(context)!!){
            when(code){
                111 ->   //시간이 밥때일 때
                    if (time.toString().substring(0,2) in mealTime){
                        postTimeAlarmResponse(SharedPreferenceController.getCurrentUserId(context)!!, context)
                    }

//            121 ->
//                //주말
//                if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){
//                    getSpecificScrapResponse(98, context)
//                }
//
//            131-> //random 알람
//                getSpecificScrapResponse(98, context)

                //테스트용 버튼누를 때
                141-> {
                    requestLocationUpdate(context)
                }

                //공유저장소 초대 알림
                151 -> {
                    ApplicationController.notificationManager.cancel(NOTICATION_ID)
                    val sharingName: String = intent.getStringExtra("sharing_name")!!
                    Log.d("jsonobject", sharingName.toString())
                    postInvitationAcceptanceResponse(SharedPreferenceController.getUserId(context)!!, sharingName)
                }

                161 -> {
                    ApplicationController.notificationManager.cancel(NOTICATION_ID)

                }

                444 -> {
                    ApplicationController.notificationManager.cancel(NOTICATION_ID)
                }
            }
        }
        Log.d("code", code.toString())

   }

    private fun requestLocationUpdate(context: Context){
        val locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for((i, location) in it.locations.withIndex()) {
                        Log.d("location", "#$i ${location.latitude} , ${location.longitude}")
                    }
                }
            }
        }

        if (getCurrentLocation(context) != null && locationCallback != null) {
            fusedLocationProviderClient = getCurrentLocation(context)!!
            fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        }
    }

    private fun getCurrentLocation(context: Context) : FusedLocationProviderClient{

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if(location == null) {
                    Log.e("location", "location get fail")
                }
                else {
                    val preLatitude = SharedPreferenceController.getUserLatitude(context)
                    val preLongitude = SharedPreferenceController.getUserLongitude(context)

                    val preLocation = Location("present location")
                    if (preLongitude != null && preLatitude!= null) {
                        preLocation.longitude = preLongitude
                        preLocation.latitude = preLatitude
                    }

                    val currLocation = Location("current location")
                    currLocation.latitude = location.latitude
                    currLocation.longitude = location.longitude

                    val distance = preLocation.distanceTo(currLocation)

//                    if(distance > 1000){
//                        //alarm request
//                        postLocationAlarmisNullResponse(SharedPreferenceController.getCurrentUserId(context)!!,
//                            location.latitude.toFloat(), location.longitude.toFloat(), context)
//                    }

                    postLocationAlarmisNullResponse(SharedPreferenceController.getCurrentUserId(context)!!,
                        location.latitude.toFloat(), location.longitude.toFloat(), context)

                    SharedPreferenceController.setUserLatitude(context, location.latitude) //distance가 100이상일 때만 업데이트할지 말지 결정하기
                    SharedPreferenceController.setUserLongitude(context, location.longitude)
                    Log.d("location", "${location.latitude} , ${location.longitude}")
                }
            }

            .addOnFailureListener {
                Log.e("location", "location error is ${it.message}")
                it.printStackTrace()
            }

        return fusedLocationClient
    }

    private fun makeDeleteNotification(context: Context, titleList : ArrayList<String>){
        var builder = NotificationCompat.Builder(context, "멤멤")
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("다음 글이 삭제되었어요 !")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (titleList != null) {
            builder.setContentText(titleList[0])
            NotificationManagerCompat.from(context).notify(NOTICATION_ID, builder.build())
        }
    }


    private fun postLocationAlarmisNullResponse(id: Int, latitude : Float, longitude : Float, context: Context) {

        var jsonObject = JSONObject()
        jsonObject.put("latitude", latitude)
        jsonObject.put("longitude", longitude)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postLocationAlarmisNullResponse = networkService.postLocationAlarmisNullResponse("application/json", id, gsonObject)

        postLocationAlarmisNullResponse.enqueue(object : Callback<PostLocationAlarmisNullResponse> {
            override fun onFailure(call: Call<PostLocationAlarmisNullResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<PostLocationAlarmisNullResponse>,
                response: Response<PostLocationAlarmisNullResponse>
            ) {
                if (response.isSuccessful && response.body()!!.status == 200) {
                    Log.d("babo", response.body().toString())

                        val intent = Intent(context, NotificationListActivity::class.java)
                        intent.putExtra("latitude", latitude)
                        intent.putExtra("longitude", longitude)

                        val pendingIntent: PendingIntent = PendingIntent.getActivity(
                            context,
                            0,  //default값 0을 삽입
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        var builder = NotificationCompat.Builder(context, "멤멤")
                            .setSmallIcon(R.drawable.ic_noti)
                            .setContentTitle("지금 다시 보면 좋은 글이에요")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)

                        NotificationManagerCompat.from(context).notify(NOTICATION_ID, builder.build())
                }

                else {
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun postTimeAlarmResponse(id: Int, context: Context) {

        val getTimeAlarmResponse = networkService.getTimeAlarmResponse("application/json", id)

        getTimeAlarmResponse.enqueue(object : Callback<GetTimeAlarmResponse> {

            override fun onFailure(call: Call<GetTimeAlarmResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<GetTimeAlarmResponse>,
                response: Response<GetTimeAlarmResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("babo", response.body().toString())

                    val data: GetTimeAlarmResponse? = response.body()

                    var builder = NotificationCompat.Builder(context, "멤멤")
                        .setSmallIcon(R.drawable.ic_noti)
                        .setContentTitle("지금 다시 보면 좋은 글이에요")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                    if (data != null) {
                        Glide.with(context)
                            .asBitmap()
                            .load(data.scrap.thumbnail)
                            .into(object : SimpleTarget<Bitmap?>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?
                                ) {
                                    val bitmap = getCircularBitmap(resource!!)
                                    builder.setContentText(data?.scrap.title)
                                    builder.setLargeIcon(bitmap)
                                    NotificationManagerCompat.from(context).notify(NOTICATION_ID, builder.build())
                                }
                            })
                    }
                }

                else {
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun postInvitationAcceptanceResponse(id: Int, sharingName: String){

        var jsonObject = JSONObject()
        jsonObject.put("sharing_name", sharingName)

        Log.d("jsonobject", jsonObject.toString())

        var gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        var postInvitationAcceptanceResponse = networkService.postInvitationAcceptanceResponse("application/json", id, gsonObject)

        postInvitationAcceptanceResponse.enqueue(object : Callback<PostSignUpResponse> {

            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable) {
                Log.e("post invitation fail", t.toString())
            }

            override fun onResponse(
                call: Call<PostSignUpResponse>,
                response: Response<PostSignUpResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("babo", response.body().toString())
                }
            }
        })
    }
}


