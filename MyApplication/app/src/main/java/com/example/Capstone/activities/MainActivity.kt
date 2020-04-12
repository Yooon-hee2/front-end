package com.example.Capstone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.model.Feed
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.content_activity_main.switch_album_or_feed
import kotlinx.android.synthetic.main.nav_drawer.*
import kotlinx.android.synthetic.main.toolbar_with_hamburger.*

class MainActivity : AppCompatActivity(){

    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

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

        btn_hambuger.setOnClickListener {
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

        switch_album_or_feed.setOnClickListener {
            switch_album_or_feed.isChecked = !switch_album_or_feed.isChecked
        }

    }

}
