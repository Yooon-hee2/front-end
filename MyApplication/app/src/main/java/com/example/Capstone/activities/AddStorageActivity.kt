package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Capstone.R
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*

class AddStorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_storage)

        btn_back.setOnClickListener {
            finish()
        }
    }
}
