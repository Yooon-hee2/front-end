package com.example.Capstone.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R
import com.example.Capstone.adapter.HashtagRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.data.TagData
import com.example.Capstone.network.get.GetAllFolderListResponse
import com.example.Capstone.network.post.PostFolderResponse
import com.example.Capstone.network.post.PostScrapResponse
import com.example.Capstone.network.put.PutScrapInfoResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowMsgActivity : AppCompatActivity() {

    private var displayWidth : Int = 0
    private var displayHeight : Int = 0

    private lateinit var copiedUrl : String
    lateinit var hashTagRecyclerViewAdapter: HashtagRecyclerViewAdapter

    private var title : String = ""
    private var scrapId : Int = 0
    private var folderId : Int = 0
    private var storageId : Int = 0

    var hashtagList : ArrayList<String> = ArrayList()
    private var folderList : HashMap<String, Int> = HashMap()
    lateinit var storageList : HashMap<String, Int>

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storageList = SharedPreferenceController.getUserStorageInfo(this)
        getAllFolderListResponse(SharedPreferenceController.getUserId(this)!!)

        var intent: Intent = intent

        var action = intent.action
        var type = intent.type

        var disp : DisplayMetrics = applicationContext.resources.displayMetrics
        displayWidth = disp.widthPixels
        displayHeight = disp.heightPixels

        storageId = SharedPreferenceController.getUserId(this)!!
        folderId = SharedPreferenceController.getUserFolderInfo(this)["전체"]!!

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                copiedUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                showSettingPopup()
            }
        }
        hashTagRecyclerViewAdapter = HashtagRecyclerViewAdapter(this, hashtagList, true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            var x = event.x
            var y = event.y

            var bitmapScreen : Bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888)

            if(x < 0 || y < 0) {
                return false
            }
            var color : Int = bitmapScreen.getPixel(x.toInt(), y.toInt())

            if(Color.alpha(color) == 0) {
                finish()
            }
            return true;
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    private fun showSettingPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_popup)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val saved : TextView = dialog.findViewById(R.id.btn_save)
        saved.setOnClickListener{
            scrapResponseData("save")
            dialog.cancel()
//            toast("저장됨!")
//            finish()
        }

        val edit : TextView = dialog.findViewById(R.id.btn_edit)
        edit.setOnClickListener {
            scrapResponseData("edit")
            dialog.cancel()
        }
    }

    private fun showModifyingPopup(title : String, hashTag : ArrayList<TagData>){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_popup)

        val editTitle : EditText = dialog.findViewById(R.id.et_edit_title)
        val recyclerviewContainer : RecyclerView = dialog.findViewById(R.id.rv_hashtag_container_dialog)
        val editNSave : TextView = dialog.findViewById(R.id.btn_submit_edit)
        val btnAddHashTag : ImageView = dialog.findViewById(R.id.btn_add)
        val storageName : TextView = dialog.findViewById(R.id.storage_name_modify)
        val folderName : TextView = dialog.findViewById(R.id.folder_name_modify)
        val createFolder : TextView = dialog.findViewById(R.id.btn_add_folder_dialog)
        val editHashTag : EditText = dialog.findViewById(R.id.edt_add_hashtag)

        folderName.text = "전체"
        storageName.text = SharedPreferenceController.getUserNickname(this) + "의 저장소"

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnAddHashTag.setOnClickListener {
            if(editHashTag.text.toString().isNotBlank()){
                var newHashTagName = editHashTag.text.toString()
                if(newHashTagName.substring(0,1) == "#"){
                    hashtagList.add(newHashTagName)
                }
                else{
                    hashtagList.add("#$newHashTagName")
                }
            }
            hashTagRecyclerViewAdapter.notifyDataSetChanged()
            editHashTag.text.clear()
        }

        editTitle.hint = title

        recyclerviewContainer.adapter = hashTagRecyclerViewAdapter
        recyclerviewContainer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        hashtagList.clear()
        if (hashTag != null) {
            for (tag in hashTag) {
                hashtagList.add(tag.tag_text)
            }
        }
        hashTagRecyclerViewAdapter.notifyDataSetChanged()

        val storageButton : ImageView = dialog.findViewById(R.id.storage_menu)
        storageButton.setOnClickListener {
            val storagePopupMenu = PopupMenu(this, storageButton)
            Log.d("storagelist", storageList.toString())
            for (storage in storageList?.keys!!){
                storagePopupMenu.menu.add(storage)
            }
            storagePopupMenu.setOnMenuItemClickListener { item ->
                storageName.text = item.title
                storageId = storageList!![item.title]!!
                getAllFolderListResponse(storageId)
                true
            }
//            getAllFolderListResponse(storageId)
//            if(!folderList.isNullOrEmpty()){
//                storagePopupMenu.show()
//            }
            storagePopupMenu.show()
//            getAllFolderListResponse(storageId)
        }

        val folderButton : ImageView = dialog.findViewById(R.id.folder_menu_popup)
        folderButton.setOnClickListener {
            val folderPopupMenu = PopupMenu(this, folderButton)
            for (folder in folderList?.keys!!){
                folderPopupMenu.menu.add(folder)
            }
            folderPopupMenu.setOnMenuItemClickListener { item ->
                folderName.text = item.title
                folderId = folderList!![item.title]!!
                true
            }
            folderPopupMenu.show()
        }

        editNSave.setOnClickListener {
            dialog.dismiss()
            if(editTitle.text.toString() == ""){
                modifyScrapResponseData(title)
            }
            else{
                modifyScrapResponseData(editTitle.text.toString())
            }
            finish()
        }

        createFolder.setOnClickListener {
            showCreateFolderDialog()
        }
    }

    private fun showSavedPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.saved_popup)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val moveToMain : TextView = dialog.findViewById(R.id.btn_move_main)
        moveToMain.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intentMain)
            finish()
        }
    }

    private fun showCreateFolderDialog() {
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


    private fun scrapResponseData(status : String) {

        var myfolderList = SharedPreferenceController.getUserFolderInfo(this)!!
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_load)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val logo = dialog.findViewById(R.id.dialog_logo) as ImageView
        val animation: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        logo.startAnimation(animation)
        dialog.show()

        var jsonObject = JSONObject()
        jsonObject.put("id", SharedPreferenceController.getUserId(this)!!.toString())
        jsonObject.put("folder_id", myfolderList["전체"]!!.toInt().toString())
        jsonObject.put("url", copiedUrl)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("bodyforscrap", gsonObject.toString())

        val postScrapResponse: Call<PostScrapResponse> =
            networkService.postScrapResponse("application/json", gsonObject)

        postScrapResponse.enqueue(object : Callback<PostScrapResponse> {
            override fun onFailure(call: Call<PostScrapResponse>, t: Throwable) {
                Log.e("fail", t.toString())
                dialog.cancel()
            }

            override fun onResponse(call: Call<PostScrapResponse>, response: Response<PostScrapResponse>) {
                if (response.isSuccessful) {
                    Log.e("crawlingbody", response.body()!!.message)
                    dialog.cancel()
                    if (response.body()?.scrap != null){
                        if (status == "edit"){
                            showModifyingPopup(response.body()!!.scrap!!.title, response.body()!!.scrap!!.tags)
                        }
                        if(status == "save"){
                            showSavedPopup()
                        }
                        scrapId = response.body()?.scrap!!.scrap_id!!
                        title = response.body()?.scrap!!.title!!
                    }
                }
                else{
                    toast("비공개 계정입니다")
                    finish()
                }
            }
        })
    }

    private fun modifyScrapResponseData(modifyingTitle: String) {

        var jsonObject = JSONObject()
        jsonObject.put("scrap_id", scrapId)

        jsonObject.put("folder", folderId)
        jsonObject.put("title", modifyingTitle)

        var tagList = hashTagRecyclerViewAdapter.returnCurrHashTag()

        val jsonArray = JSONArray()
        val jsonArrayMemos = JSONArray()

        if (tagList != null) {
            for (tag in tagList){
                var tagJsonObject = JSONObject()
                tagJsonObject.put("tag_text", tag)
                jsonArray.put(tagJsonObject)
            }
        }
        jsonObject.put("memos", jsonArrayMemos)
        jsonObject.put("tags", jsonArray)
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("bodyformodify", gsonObject.toString())

        val putScrapInfoResponse: Call<PutScrapInfoResponse> =
            networkService.putScrapInfoResponse("application/json", scrapId, gsonObject)

        putScrapInfoResponse.enqueue(object : Callback<PutScrapInfoResponse> {
            override fun onFailure(call: Call<PutScrapInfoResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PutScrapInfoResponse>, response: Response<PutScrapInfoResponse>) {
                if (response.isSuccessful) {
                    Log.e("crawlingbody", response.body().toString())
                }
            }
        })
    }

    private fun getAllFolderListResponse(id: Int){
        folderList.clear()

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
                    Log.d("babo", response.body().toString())

                    val data: ArrayList<GetAllFolderListResponse>? = response.body()
                    if (!data.isNullOrEmpty()) {
                        for(folders in data) {
                            for(folder in folders.folders){
                                if(folder.folder_key == 0){
                                    folderList["전체"] = folder.folder_id
                                }
                                else{
                                    folderList[folder.folder_name] = folder.folder_id
                                }
                            }
                        }
                    }
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
        jsonObject.put("id", storageId)
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
                        for(folder in data.folders) {
                                if(folder.folder_key == 0){
                                    folderList["전체"] = folder.folder_id
                                }
                                else{
                                    folderList[folder.folder_name] = folder.folder_id
                                }
                            }
                        SharedPreferenceController.setUserFolderInfo(this@ShowMsgActivity, folderList)
                    }
                }
            }
        })
    }
}