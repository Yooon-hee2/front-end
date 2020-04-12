package com.example.Capstone.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_folder_manage.*
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*


class FolderManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_manage)

        btn_back.setOnClickListener {
            finish()
        }

        btn_add_folder.setOnClickListener{
            showDialog()
        }
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_folder, null)
        val btn_dialog_add_folder = dialogView.findViewById<TextView>(R.id.btn_add_folder)

        builder.setView(dialogView)
        builder.setCancelable(true)
        builder.show()

        btn_dialog_add_folder.setOnClickListener {
            // request post
            finish()
            val intent = Intent(this, FolderManageActivity::class.java)
            startActivity(intent)
        }
    }
}
