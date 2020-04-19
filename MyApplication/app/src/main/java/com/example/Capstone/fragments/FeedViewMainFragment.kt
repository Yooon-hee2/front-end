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

        dataList.add(Feed(0, "instagram", "스테이크+소세지조합최고", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36",
            arrayListOf("스테이크", "이태원", "소세지")))
        dataList.add(
            Feed(1, "facebook", "중앙대국밥하면순대나라", "http://blogfiles.naver.net/MjAxNjEwMzBfMTU5/MDAxNDc3ODEzNTAyNzc5.LcGMoEbTvDQ6VSw2VPg8DmWCKFLNgMjVwDsKT9287iUg.vjmV-" +
                    "W8lxd4uzMBY3A0ZwRU3NiSHxZ-C0__kprqbZa8g.JPEG.moonfrost/DSC04123.JPG", arrayListOf("순대나라", "중앙대")
            )
        )
        dataList.add(Feed(2, "instagram", "중앙대마라탕은칠기", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36", arrayListOf()))
        dataList.add(Feed(3, "instagram", "title", "https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36", arrayListOf()))

        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(context!!, dataList)
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(context!!)

    }
    fun changeRecyclerViewData(charSequence: CharSequence){
        feedRecyclerViewAdapter.filter.filter(charSequence)
    }
}
