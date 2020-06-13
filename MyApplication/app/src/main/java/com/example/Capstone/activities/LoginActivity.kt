package com.example.Capstone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.Capstone.R
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.post.PostLoginResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_login_login
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var nickname : String = ""
    private var password : String = ""
    private var email : String = ""
    private var TOKEN : String = ""

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getFirebaseToken()

        if(SharedPreferenceController.getUserNickname(this)!!.isNotEmpty()){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY and Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        et_login_nickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nickname = et_login_nickname.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        et_login_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                password = et_login_pw.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        btn_login_login.setOnClickListener {
            loginResponseData()
        }

        btn_login_signup.setOnClickListener {
            startActivity<SignUpEmailActivity>()
            finish()
        }
    }

    private fun loginResponseData() {
        var jsonObject = JSONObject()
        jsonObject.put("username", nickname)
        jsonObject.put("password", password)
        jsonObject.put("token", TOKEN)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.e("request body", gsonObject.toString())

        val postSignUpResponse: Call<PostLoginResponse> =
            networkService.postLoginResponse("application/json", gsonObject)

        postSignUpResponse.enqueue(object : Callback<PostLoginResponse> {
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                toast("fail")
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostLoginResponse>, response: Response<PostLoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        toast("login success")
                        val nickname = nickname
                        val email = email
                        Log.e("id", response.body()!!.id.toString())
                        SharedPreferenceController.setUserNickname(this@LoginActivity, nickname)
                        SharedPreferenceController.setCurrentUserId(this@LoginActivity, response.body()!!.id)
                        SharedPreferenceController.setUserId(this@LoginActivity, response.body()!!.id)
                        SharedPreferenceController.setUserEmail(this@LoginActivity, response.body()!!.email)
                        startActivity<MainActivity>()
                        finish()
                    }
                }
            }
        })
    }

    private fun getFirebaseToken(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcm", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                TOKEN = token.toString()
                Log.d("fcm", TOKEN)
                SharedPreferenceController.setUserToken(this@LoginActivity, TOKEN)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
    }
}
