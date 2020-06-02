package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.NotificationRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Feed
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetSpecificScrapResponse
import com.example.Capstone.network.post.PostLocationAlarmResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_notification_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    lateinit var notiListRecyclerViewAdapter: NotificationRecyclerViewAdapter
    private var dataList : ArrayList<Feed> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        val latitude = intent.getFloatExtra("latitude", 1.0F)
        val longitude = intent.getFloatExtra("longitude", 1.0F)

        postLocationAlarmResponse(SharedPreferenceController.getCurrentUserId(this)!!, latitude, longitude)

        notiListRecyclerViewAdapter = NotificationRecyclerViewAdapter(this, dataList)
        rv_noti_container.adapter = notiListRecyclerViewAdapter
        rv_noti_container.layoutManager = LinearLayoutManager(this)
    }

    private fun updateDataList(list: ArrayList<Feed>){
        dataList.clear()
        dataList.addAll(list)
        notiListRecyclerViewAdapter.notifyDataSetChanged()
    }
    private fun postLocationAlarmResponse(id: Int, latitude : Float, longitude : Float) {

        var jsonObject = JSONObject()
        jsonObject.put("latitude", latitude)
        jsonObject.put("longitude", longitude)

        Log.d("alarmlist", jsonObject.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postLocationAlarmResponse = networkService.postLocationAlarmResponse("application/json", id, gsonObject)

        postLocationAlarmResponse.enqueue(object : Callback<PostLocationAlarmResponse> {
            override fun onFailure(call: Call<PostLocationAlarmResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<PostLocationAlarmResponse>,
                response: Response<PostLocationAlarmResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("alarmlist", response.body()!!.scraps.toString())

                    val data: ArrayList<GetSpecificScrapResponse> = response.body()!!.scraps

                    val tempDataList : ArrayList<Feed> = ArrayList()

                    for(scrap in data) {
                        tempDataList.add(scrap.toFeedDetail())
                        updateDataList(tempDataList)
                    }
                    notiListRecyclerViewAdapter.notifyDataSetChanged()
                }

                else {
                    Log.e("error", "fail")
                }
            }
        })
    }
}

//                    if (data != null) {
//                        Glide.with(context)
//                            .asBitmap()
//                            .load(data.scraps!!.thumbnail)
//                            .into(object : SimpleTarget<Bitmap?>() {
//                                override fun onResourceReady(
//                                    resource: Bitmap,
//                                    transition: Transition<in Bitmap?>?
//                                ) {
//                                    val bitmap = getCircularBitmap(resource!!)
//                                    builder.setContentText(data?.scrap.title)
//                                    builder.setLargeIcon(bitmap)
//                                    NotificationManagerCompat.from(context).notify(NOTICATION_ID, builder.build())
//                                }
//                            })
//                    }
