package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.model.Feed
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dataList: ArrayList<Feed> = ArrayList()

        dataList.add(Feed("instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))
        dataList.add(Feed("instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))
        dataList.add(Feed("instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))
        dataList.add(Feed("instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36"))

        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(this, dataList)
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(this)
    }

}
