package com.example.Capstone.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
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
import com.example.Capstone.network.post.PostScrapResponse
import com.example.Capstone.network.put.PutScrapInfoResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.content_activity_main.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.w3c.dom.Text
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

    var hashtagList : ArrayList<String> = ArrayList()
    lateinit var folderList : HashMap<String, Int>

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        folderList = SharedPreferenceController.getUserFolderInfo(this)

        var intent: Intent = intent

        var action = intent.action
        var type = intent.type

        var disp : DisplayMetrics = applicationContext.resources.displayMetrics
        displayWidth = disp.widthPixels
        displayHeight = disp.heightPixels

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                copiedUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                showSettingPopup()
            }
        }

        hashTagRecyclerViewAdapter = HashtagRecyclerViewAdapter(this, hashtagList)
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
//            showSavedPopup()
            dialog.cancel()
//            toast("저장됨!")
//            finish()
        }

        val edit : TextView = dialog.findViewById(R.id.btn_edit)
        edit.setOnClickListener {
            scrapResponseData("edit")
//            showModifyingPopup()
            dialog.cancel()
//            finish()
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
        val btnAdd : ImageView = dialog.findViewById(R.id.btn_add)
        val folderName : TextView = dialog.findViewById(R.id.folder_name_modify)

        folderName.text = "전체"

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnAdd.visibility = View.VISIBLE

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

        val button : ImageView = dialog.findViewById(R.id.folder_menu)
        button.setOnClickListener {
            val popupMenu = PopupMenu(this, button)
            for (folder in folderList?.keys!!){
                popupMenu.menu.add(folder)
            }
            popupMenu.setOnMenuItemClickListener { item ->
                folderName.text = item.title
                folderId = folderList!![item.title]!!
                true
            }
            popupMenu.show()
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
    }

    private fun createNewTextView(text: String): TextView? {
        val lparams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val textView = TextView(this)
        textView.layoutParams = lparams
        textView.text = "$text"
        textView.textSize = resources.getDimension(R.dimen.big_font)
        textView.setTextColor(resources.getColor(R.color.black))
        return textView
    }

    private fun showSavedPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.saved_popup)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        Handler().postDelayed({
//            dialog.dismiss()
//            finish()
//        }, 3000) //error 에러생김

        val moveToMain : TextView = dialog.findViewById(R.id.btn_move_main)
        moveToMain.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intentMain)
            finish()
        }
    }

    private fun scrapResponseData(status : String) {

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
        jsonObject.put("folder_id", folderList["전체"].toString().substring(0,1))
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
                    Log.e("crawlingbody", response.body().toString())
                    dialog.cancel()
                    if (status == "edit"){
                        showModifyingPopup(response.body()!!.scrap.title, response.body()!!.scrap.tags)
                    }
                    if(status == "save"){
                        showSavedPopup()
                    }
                    scrapId = response.body()?.scrap?.scrap_id!!
                    title = response.body()?.scrap?.title!!
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
        if (folderId == 0){
            jsonObject.put("folder", folderList["전체"].toString().substring(0,1))
        }
        else{
            jsonObject.put("folder", folderId)
        }
        jsonObject.put("title", modifyingTitle)

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
}