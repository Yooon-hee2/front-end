package com.example.Capstone.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_folder_manage.*
import kotlinx.android.synthetic.main.nav_drawer.view.*
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

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_folder)

        val newFolderName = dialog.findViewById(R.id.et_new_folder) as EditText

        val submit = dialog.findViewById(R.id.btn_add_folder) as TextView
        submit.setOnClickListener {
            dialog.dismiss()
            folder_4th.visibility = View.VISIBLE
            folder_4th_txt.text = "새폴더"
//            val intent = Intent(this, FolderManageActivity::class.java)
//            startActivity(intent)
        }
        dialog.show()
    }
}
