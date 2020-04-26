package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.Capstone.R
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.PostSignUpResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_sign_up_name.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpNameActivity : AppCompatActivity() {

    private var et_nickname : String = ""
    private var email : String = ""
    private var password : String = ""

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_name)


        et_nickname = findViewById<EditText>(R.id.et_signup_nickname).text.toString()
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")

        btn_signup_finish.setOnClickListener {

        }
    }
//    private fun SignUpResponseData() {
//        var jsonObject = JSONObject()
//        jsonObject.put("email", send_email)
//        jsonObject.put("password", send_pw)
//        jsonObject.put("nickname", send_nickname)
//
//        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
//
//        val postSignUpResponse: Call<PostSignUpResponse> =
//            networkService.postSignupResponse("application/json", gsonObject)

//        postSignUpResponse.enqueue(object : Callback<PostSignUpResponse> {
//            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable) {
//                Log.e("Sign fail", t.toString())
//                ColorToast(this@SignUpActivity, "잠시 후 다시 접속해주세요")
//            }
//
//            override fun onResponse(call: Call<PostSignUpResponse>, response: Response<PostSignUpResponse>) {
//                if (response.isSuccessful) {
//                    if (response.body()!!.status == Secret.NETWORK_SUCCESS) {
//                        ColorToast(this@SignUpActivity, "회원가입 되셨습니다")
//                        startActivity<LoginActivity>()
//                        finish()
//                    } else {
//                        ColorToast(this@SignUpActivity, response.body()!!.message)
//                        startActivity<LoginActivity>()
//                        finish()
//                    }
//
//                }
//            }
//        })
//    }
}
