package com.example.Capstone.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R
import com.example.Capstone.activities.MainActivity
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Storage
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllStorageListResponse
import com.example.Capstone.network.post.PostLocationAlarmisNullResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StorageRecyclerViewAdapter(val ctx: Context, var storageList: ArrayList<Storage>) : RecyclerView.Adapter<StorageRecyclerViewAdapter.Holder>() {

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_folder, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = storageList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.storageName.text = storageList[position].sharing_name
//        holder.deleteButton.visibility = View.GONE

        holder.storageContainer.setOnClickListener{
            (ctx as Activity).finish()
            SharedPreferenceController.setCurrentUserId(ctx, storageList[position].id)
            val intent = Intent(ctx, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            holder.storageContainer.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            storageDeleteResponseData(SharedPreferenceController.getUserId(ctx)!!, storageList[position].sharing_name)
            storageList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storageName = itemView.findViewById(R.id.rv_item_folder_name) as TextView
        var deleteButton = itemView.findViewById(R.id.btn_delete_folder) as ImageView
        var storageContainer = itemView.findViewById(R.id.rv_item_folder) as RelativeLayout
    }

    private fun storageDeleteResponseData(userId : Int, sharing_name : String) {

        var jsonObject = JSONObject()
        jsonObject.put("sharing_name", sharing_name)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val deleteStorageResponse: Call<ArrayList<GetAllStorageListResponse>> =
            networkService.deleteStorageResponse("application/json", userId, gsonObject)

        deleteStorageResponse.enqueue(object : Callback<ArrayList<GetAllStorageListResponse>> {
            override fun onFailure(call: Call<ArrayList<GetAllStorageListResponse>>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<GetAllStorageListResponse>>, response: Response<ArrayList<GetAllStorageListResponse>>) {
                if (response.isSuccessful) {
                    storageList.clear()

                    val data : ArrayList<GetAllStorageListResponse>? = response.body()
                    if(!data.isNullOrEmpty()){
                        for(storage in data){
                            storageList.add(Storage(storage.id, storage.sharing_name))
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        })
    }
}