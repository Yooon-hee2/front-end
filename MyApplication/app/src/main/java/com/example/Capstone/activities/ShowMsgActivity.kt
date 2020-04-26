package com.example.Capstone.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.example.Capstone.R
import kotlinx.android.synthetic.main.alert_popup.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class ShowMsgActivity : AppCompatActivity() {

    private var displayWidth : Int = 0
    private var displayHeight : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent: Intent = getIntent()

        var action = intent.action
        var type = intent.type

        var disp : DisplayMetrics = applicationContext.resources.displayMetrics
        displayWidth = disp.widthPixels
        displayHeight = disp.heightPixels

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {
//                var text: String = intent.getStringExtra(Intent.EXTRA_TEXT)
////                toast(text)
//                AlertDialog.Builder(this)
//                            .setTitle("저장됨!")
//                            .setMessage(text)
//                            .setPositiveButton(android.R.string.ok,null)
//                            .setMessage("저장됨!")
//                            .setCancelable(true)
//                            .create()
//                            .show()
                showSettingPopup()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            var x = event.x
            var y = event.y

            var bitmapScreen : Bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888)

            if(x < 0 || y < 0) {
                return false
            }
            var ARGB : Int = bitmapScreen.getPixel(x.toInt(), y.toInt())

            if(Color.alpha(ARGB) == 0) {
                finish()
            }
            return true;
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

//    private fun showSettingPopup(){
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
//
//        val view = inflater.inflate(R.layout.alert_popup, null)
////        val textView: TextView = view.findViewById(R.id.textView)
////        textView.text = "저장됨"
//
//        val alertDialog = AlertDialog.Builder(this)
//            .create()
//        alertDialog.setView(view)
//        alertDialog.show()
////        alertDialog.window?.setLayout()
//        alertDialog.window?.setGravity(Gravity.BOTTOM)
//        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val saved : TextView = view.findViewById(R.id.btn_save)
//        saved.setOnClickListener{
//            alertDialog.cancel()
//            toast("저장됨!")
//            finish()
//
//        }
//
//        val edit : TextView = view.findViewById(R.id.btn_edit)
//        edit.setOnClickListener {
//            alertDialog.cancel()
//            val edit_inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
//            val edit_view = edit_inflater.inflate(R.layout.edit_popup,null)
//            val editDialog = AlertDialog.Builder(this).create()
//            editDialog.setView(edit_view)
//            editDialog.show()
//            editDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        }
//    }

    private fun showSettingPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_popup)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val saved : TextView = dialog.findViewById(R.id.btn_save)
        saved.setOnClickListener{
            dialog.cancel()
            toast("저장됨!")
            finish()
        }

        val edit : TextView = dialog.findViewById(R.id.btn_edit)
        edit.setOnClickListener {
            showModifyingPopup()
            finish()
        }
    }

    private fun showModifyingPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_popup)

        dialog.show()

        val edit_n_save : TextView = dialog.findViewById(R.id.btn_submit_edit)
        edit_n_save.setOnClickListener {
            dialog.dismiss()
            finish()
        }
    }
}