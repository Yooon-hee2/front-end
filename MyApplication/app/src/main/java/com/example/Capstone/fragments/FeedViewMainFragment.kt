package com.example.Capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.model.Feed
import kotlinx.android.synthetic.main.fragment_feed_view_main.*
import org.jetbrains.anko.support.v4.ctx


/**
 * A simple [Fragment] subclass.
 */
class FeedViewMainFragment : Fragment() {

    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_view_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var dataList: ArrayList<Feed> = ArrayList()

        dataList.add(Feed(0, "instagram", "이태원 스테이크 맛집", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36",
            arrayListOf("스테이크", "이태원", "소세지")))
        dataList.add(
            Feed(1, "facebook", "중앙대 국밥하면 순대나라", "http://blogfiles.naver.net/MjAxNjEwMzBfMTU5/MDAxNDc3ODEzNTAyNzc5.LcGMoEbTvDQ6VSw2VPg8DmWCKFLNgMjVwDsKT9287iUg.vjmV-" +
                    "W8lxd4uzMBY3A0ZwRU3NiSHxZ-C0__kprqbZa8g.JPEG.moonfrost/DSC04123.JPG", arrayListOf("순대나라", "중앙대")
            )
        )
        dataList.add(Feed(2, "instagram", "탱스타그램", "https://www.topstarnews.net/news/photo/201805/415426_62309_4431.jpg", arrayListOf("서가대")))
        dataList.add(Feed(3, "instagram", "동물의숲 하고싶어 엉엉", "https://lh3.googleusercontent.com/proxy/6dRaCzOlQZtZ52zySWmKPGcA0sWYOtw9tswZbdakgWLqB_D6B_" +
                "-Tj4OTbHQIZrfyzfe6APYFH3ix9EBh3UqLF3lm7ei_nBIrj5mZ0l4x5yNxFeUT01Tq3oW-prtnqxqZl9CyzxqA", arrayListOf("애플", "최고야")))

        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(context!!, dataList)
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(context!!)

    }
    fun changeRecyclerViewData(charSequence: CharSequence){
        feedRecyclerViewAdapter.filter.filter(charSequence)
    }
}
