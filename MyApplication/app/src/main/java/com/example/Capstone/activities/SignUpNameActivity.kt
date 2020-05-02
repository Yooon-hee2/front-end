package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.Capstone.R
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.post.PostSignUpResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_sign_up_name.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpNameActivity : AppCompatActivity() {

    private var nickname : String = ""
    private var email : String = ""
    private var password : String = ""

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_name)


        val et_nickname = findViewById<EditText>(R.id.et_signup_nickname)
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")

        btn_signup_finish.setOnClickListener {
            SignUpResponseData()
        }

        btn_signup_back.setOnClickListener {
            onBackPressed()
        }

        et_nickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nickname = et_nickname.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
    private fun SignUpResponseData() {
        var jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        jsonObject.put("username", nickname)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.e("request body", gsonObject.toString())

        val postSignUpResponse: Call<PostSignUpResponse> =
            networkService.postSignupResponse("application/json", gsonObject)

        postSignUpResponse.enqueue(object : Callback<PostSignUpResponse> {
            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable) {
                toast("fail")
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostSignUpResponse>, response: Response<PostSignUpResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        toast("sign up success")
                        startActivity<LoginActivity>()
                        finish()
                    }
                }
            }
        })
    }
}
