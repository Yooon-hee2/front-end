package com.example.Capstone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_storage_manage.*
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*

class StorageManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_manage)

        btn_back.setOnClickListener {
            finish()
        }

        btn_add_share_storage.setOnClickListener {
            val intent = Intent(this, AddStorageActivity::class.java)
            startActivity(intent)
        }

    }
}
