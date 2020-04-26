package com.example.Capstone.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import org.jetbrains.anko.intentFor

class SignUpEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)

        val et_email = findViewById<EditText>(R.id.et_signup_email)

        btn_signup_email.setOnClickListener {
            startActivity(intentFor<SignUpPasswordActivity>("email" to et_email.text.toString()))
        }
    }
}
