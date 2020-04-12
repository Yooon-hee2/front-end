package com.example.Capstone.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_folder_manage.*
import kotlinx.android.synthetic.main.activity_folder_manage.btn_add_folder
import kotlinx.android.synthetic.main.dialog_add_folder.*
import kotlinx.android.synthetic.main.toolbar_with_onlyback.*
import org.w3c.dom.Text


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

        val btn_dialog_close = dialogView.findViewById<TextView>(R.id.btn_cancel)
        val btn_dialog_add_folder = dialogView.findViewById<TextView>(R.id.btn_add_folder)

        builder.setView(dialogView)
        builder.show()

        btn_dialog_close.setOnClickListener {
            showDialog()
        }

        btn_dialog_add_folder.setOnClickListener {
            finish()
        }
    }
}
