package com.example.Capstone.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.Capstone.R
import com.example.Capstone.adapter.HashtagRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllStorageListResponse
import com.example.Capstone.network.post.PostLocationAlarmisNullResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_add_storage.*
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStorageActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    lateinit var hashTagRecyclerViewAdapter: HashtagRecyclerViewAdapter

    var hashtagList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_storage)

        btn_add_member.setOnClickListener {
            et_add_member.text.clear()
            var memberName = et_add_member.text.toString()
            postMemberisExistResponse(memberName)
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_storage_submit.setOnClickListener {
            var storageName = et_storage_name.text.toString()
            var memberList = hashTagRecyclerViewAdapter.returnCurrHashTag()
            if(!TextUtils.isEmpty(storageName) && memberList != null){
                postNewStorageResponse(storageName, memberList)
            }
            else if (TextUtils.isEmpty(storageName)){
                toast("저장소 이름을 입력해 주세요")
            }
            else if (memberList == null){
                toast("공유할 멤버를 입력해 주세요")
            }
        }

        btn_profile_img_change.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        hashTagRecyclerViewAdapter = HashtagRecyclerViewAdapter(this, hashtagList, true)
        rv_container_member.adapter = hashTagRecyclerViewAdapter
        rv_container_member.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    toast("Permission denied")
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //img_profile.setImageURI(data?.data)
            Glide.with(this).load(data?.data)
                .apply(RequestOptions.circleCropTransform()).into(img_profile)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun postMemberisExistResponse(member : String) {

        var jsonObject = JSONObject()
        jsonObject.put("username", member)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postMemberisExistResponse = networkService.postMemberisExistedResponse("application/json", gsonObject)

        Log.d("name", jsonObject.toString())

        postMemberisExistResponse.enqueue(object : Callback<PostLocationAlarmisNullResponse> {
            override fun onFailure(call: Call<PostLocationAlarmisNullResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<PostLocationAlarmisNullResponse>,
                response: Response<PostLocationAlarmisNullResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()!!.status == 200){
                        hashtagList.add(member)
                        hashTagRecyclerViewAdapter.notifyDataSetChanged()
                    }
                    else{
                        toast("존재하지 않는 닉네임입니다.")
                    }
                }

                else {
                    Log.e("error", "fail")
                }
            }
        })
    }


    private fun postNewStorageResponse(storageName : String, memberList: ArrayList<String>) {

        var jsonObject = JSONObject()
        jsonObject.put("sharing_name", storageName)

        val jsonArrayNames = JSONArray()

        var nameJsonObject = JSONObject()
        nameJsonObject.put("username", SharedPreferenceController.getUserNickname(this))
        jsonArrayNames.put(nameJsonObject)

        for (name in memberList!!){
            var nameJsonObject = JSONObject()
            nameJsonObject.put("username", name)
            jsonArrayNames.put(nameJsonObject)
        }

        jsonObject.put("users", jsonArrayNames)

        Log.d("addstorage", jsonObject.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postNewStorageResponse = networkService.postNewStorageResponse("application/json", gsonObject)

        postNewStorageResponse.enqueue(object : Callback<PostLocationAlarmisNullResponse> {
            override fun onFailure(call: Call<PostLocationAlarmisNullResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<PostLocationAlarmisNullResponse>,
                response: Response<PostLocationAlarmisNullResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()!!.status == 200){
                        toast("새 공유저장소가 생성되었습니다 !")
                        finish()
                    }
                }

                else {
                    Log.e("error", "fail")
                }
            }
        })
    }
}
