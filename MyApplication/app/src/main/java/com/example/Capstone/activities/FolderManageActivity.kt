package com.example.Capstone.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.adapter.FolderRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.data.FolderData
import com.example.Capstone.network.get.GetAllFolderListResponse
import com.example.Capstone.network.post.PostFolderResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_folder_manage.*
import kotlinx.android.synthetic.main.nav_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FolderManageActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    lateinit var folderRecyclerViewAdapter : FolderRecyclerViewAdapter
    private var folderList : ArrayList<FolderData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_manage)

        btn_back.setOnClickListener {
            finish()
        }

        btn_add_folder.setOnClickListener{
            showDialog()
        }

        folderRecyclerViewAdapter = FolderRecyclerViewAdapter(this, folderList)
        rv_folder_container.adapter = folderRecyclerViewAdapter
        rv_folder_container.layoutManager = LinearLayoutManager(this)

        getAllFolderListResponse(SharedPreferenceController.getUserId(this)!!)
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_folder)

        val newFolderName = dialog.findViewById(R.id.et_new_folder) as EditText

        val submit = dialog.findViewById(R.id.btn_add_folder) as TextView
        submit.setOnClickListener {
            dialog.dismiss()
            if(newFolderName.text.toString().isNotEmpty())
            folderResponseData(newFolderName.text.toString())
        }
        dialog.show()
    }

    private fun getAllFolderListResponse(id: Int){
        val getAllFolderListResponse = networkService.getAllFolderListResponse(id)

        getAllFolderListResponse.enqueue(object : Callback<ArrayList<GetAllFolderListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetAllFolderListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetAllFolderListResponse>>,
                response: Response<ArrayList<GetAllFolderListResponse>>
            ) {
                if(response.isSuccessful){
                    folderList.clear()
                    Log.d("babo", response.body().toString())
                    val data: ArrayList<GetAllFolderListResponse>? = response.body()
                    if (data != null) {
                        for(folders in data) {
                            for(folder in folders.folders){
                                folderList.add(folder)
                            }
                        }
                    }
                    folderRecyclerViewAdapter.notifyDataSetChanged()
                }
                else{
                    Log.e("error", "fail")
                }
            }
        })
    }

    //create new folder
    private fun folderResponseData(folderName : String) {

        var jsonObject = JSONObject()
        jsonObject.put("id", SharedPreferenceController.getUserId(this)!!.toString())
        jsonObject.put("folder_name", folderName)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("bodyforNewFolder", gsonObject.toString())

        val folderScrapResponse: Call<PostFolderResponse> =
            networkService.postFolderResponse("application/json", gsonObject)

        folderScrapResponse.enqueue(object : Callback<PostFolderResponse> {
            override fun onFailure(call: Call<PostFolderResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostFolderResponse>, response: Response<PostFolderResponse>) {
                if (response.isSuccessful) {
                    folderList.clear()
                    Log.e("addNewFolder", response.body().toString())
                    val data: PostFolderResponse? = response.body()
                    if (data != null) {
                        for(folders in data.folders) {
                            folderList.add(folders)
                        }
                    }
                    folderRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
