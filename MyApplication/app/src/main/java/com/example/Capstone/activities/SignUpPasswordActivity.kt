package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_sign_up_password.*
import org.jetbrains.anko.intentFor

class SignUpPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_password)


        val et_password = findViewById<EditText>(R.id.et_signup_password)
        val email = intent.getStringExtra("email")

        btn_signup_password.setOnClickListener {
            startActivity(intentFor<SignUpNameActivity>(
                "email" to email,
                "password" to et_password.text.toString()))
            finish()
        }

        btn_signup_back.setOnClickListener {
            onBackPressed()
        }
    }
}
