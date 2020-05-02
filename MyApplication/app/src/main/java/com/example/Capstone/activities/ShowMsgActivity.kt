package com.example.Capstone.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.Capstone.R
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.post.PostScrapResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowMsgActivity : AppCompatActivity() {

    private var displayWidth : Int = 0
    private var displayHeight : Int = 0

    private lateinit var hashtag1: TextView
    private lateinit var hashtag2: TextView
    private lateinit var hashtag3: TextView
    private lateinit var hashtag4: TextView
    private lateinit var hashtag5: TextView
    private lateinit var hashtagTextList: ArrayList<TextView>

    private lateinit var sharedUrl : String

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent: Intent = intent

        var action = intent.action
        var type = intent.type

        var disp : DisplayMetrics = applicationContext.resources.displayMetrics
        displayWidth = disp.widthPixels
        displayHeight = disp.heightPixels

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                showSettingPopup()
            }
        }

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
            scrapResponseData()
            showSavedPopup()
            dialog.cancel()
//            toast("저장됨!")
//            finish()
        }

        val edit : TextView = dialog.findViewById(R.id.btn_edit)
        edit.setOnClickListener {
            showModifyingPopup()
            dialog.cancel()
//            finish()
        }
    }

    private fun showModifyingPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_popup)

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        hashtag1 = dialog.findViewById(R.id.add_hashtag1)
        hashtag2 = dialog.findViewById(R.id.add_hashtag2)
        hashtag3 = dialog.findViewById(R.id.add_hashtag3)
        hashtag4 = dialog.findViewById(R.id.add_hashtag4)
        hashtag5 = dialog.findViewById(R.id.add_hashtag5)
        hashtagTextList = arrayListOf(hashtag1, hashtag2, hashtag3, hashtag4, hashtag5)

        val addHashtag : EditText = dialog.findViewById(R.id.edt_add_hashtag)
        val editNSave : TextView = dialog.findViewById(R.id.btn_submit_edit)
        val btnAdd : ImageView = dialog.findViewById(R.id.btn_add)
        btnAdd.visibility = View.VISIBLE

        var i = 0
        btnAdd.setOnClickListener {
            hashtagTextList[i].visibility = View.VISIBLE
            hashtagTextList[i].text = addHashtag.text.toString()
            i++
            if(i == 5){
                btnAdd.visibility = View.GONE
            }
        }

        editNSave.setOnClickListener {
            scrapResponseData()
            dialog.dismiss()
            finish()
        }
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

    private fun scrapResponseData() {
        var jsonObject = JSONObject()
        jsonObject.put("url", sharedUrl)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.e("request body", gsonObject.toString())

        val postScrapResponse: Call<PostScrapResponse> =
            networkService.postScrapResponse("application/json", gsonObject)

        postScrapResponse.enqueue(object : Callback<PostScrapResponse> {
            override fun onFailure(call: Call<PostScrapResponse>, t: Throwable) {
                toast("crawling failed")
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostScrapResponse>, response: Response<PostScrapResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        toast("crawling success")
                    }
                }
            }
        })
    }
}