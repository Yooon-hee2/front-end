package com.example.Capstone.activities

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.model.Feed
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.nav_drawer.*

class MainActivity : AppCompatActivity(){

    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    private val TAG = "MainActivity"
    lateinit var mIntent: Intent


//    private fun handleSendText(intent: Intent) {
//        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
//            // Update UI to reflect text being shared
//            AlertDialog.Builder(this)
//                .setTitle("MemMem")
//                .setMessage(intent.getStringExtra(Intent.EXTRA_TEXT))
//                .setPositiveButton(android.R.string.ok,null)
//                .setCancelable(false)
//                .create()
//                .show();
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var dataList: ArrayList<Feed> = ArrayList()

        dataList.add(Feed(0, "instagram", "스테이크+소세지조합최고", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))
        dataList.add(Feed(1, "facebook", "중앙대국밥하면순대나라", "http://blogfiles.naver.net/MjAxNjEwMzBfMTU5/MDAxNDc3ODEzNTAyNzc5.LcGMoEbTvDQ6VSw2VPg8DmWCKFLNgMjVwDsKT9287iUg.vjmV-" +
                "W8lxd4uzMBY3A0ZwRU3NiSHxZ-C0__kprqbZa8g.JPEG.moonfrost/DSC04123.JPG"))
        dataList.add(Feed(2, "instagram", "중앙대마라탕은칠기", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))
        dataList.add(Feed(3, "instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))

        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(this, dataList)
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(this)

        btn_hamburger.setOnClickListener {
            if (!ly_drawer.isDrawerOpen(GravityCompat.END)) {
                ly_drawer.openDrawer(GravityCompat.END)
            }
//            if ((SharedPreferenceController.getUserImg(this))!!.isNotEmpty()) {
//                Glide.with(this@MainActivity)
//                    .load(SharedPreferenceController.getUserImg(this))
//                    .apply(RequestOptions.circleCropTransform())?.into(iv_drawer_profileimg)
//            } else {
//                Glide.with(this@MainActivity)
//                    .load(R.drawable.pofile)
//                    .apply(RequestOptions.circleCropTransform())?.into(iv_drawer_profileimg)
//            }
        }

        btn_personal_storage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_share_storage.setOnClickListener {
            val intent = Intent(this, StorageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_manage_folder.setOnClickListener {
            val intent = Intent(this, FolderManageActivity::class.java)
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_manage_storage.setOnClickListener {
            val intent = Intent(this, StorageManageActivity::class.java)
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_cancel.setOnClickListener {
            if (ly_drawer.isDrawerOpen(GravityCompat.END)) {
                ly_drawer.closeDrawer(GravityCompat.END)
            }
        }

        var spinnerList = arrayOf("맛집", "화장품", "꿀팁")

        val spinner : Spinner = findViewById(R.id.spinner_menu)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        spinner.adapter = spinnerAdapter

        myNotification()

    }

    private fun myNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("memmem", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        var builder = NotificationCompat.Builder(this, "memmem")
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("memmem notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("오늘 저녁식사 메뉴를 정하지 못했다면 ?."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        // App을 종료할 때 서비스(ClipboardService)를 종료
        stopService(mIntent)
    }


}


fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            Log.e("isServiceRunning", "Service is running")
            return true
        }
    }
    return false
}