package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.model.Feed
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_with_hamburger.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

class MainActivity : AppCompatActivity() {

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
        }

        switch_album_or_feed.setOnClickListener {
            switch_album_or_feed.isChecked = !switch_album_or_feed.isChecked
        }

    }

}
