package com.example.Capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.AlbumRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_album_view_main.*


/**
 * A simple [Fragment] subclass.
 */
class AlbumViewMainFragment : Fragment() {

    lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_view_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var dataList: ArrayList<String> = ArrayList()

        dataList.add("https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36")
        dataList.add("http://blogfiles.naver.net/MjAxNjEwMzBfMTU5/MDAxNDc3ODEzNTAyNzc5.LcGMoEbTvDQ6VSw2VPg8DmWCKFLNgMjVwDsKT9287iUg.vjmV-" +
                "W8lxd4uzMBY3A0ZwRU3NiSHxZ-C0__kprqbZa8g.JPEG.moonfrost/DSC04123.JPG")
        dataList.add("https://www.topstarnews.net/news/photo/201805/415426_62309_4431.jpg")
        dataList.add("https://lh3.googleusercontent.com/proxy/6dRaCzOlQZtZ52zySWmKPGcA0sWYOtw9tswZbdakgWLqB_D6B_" +
                "-Tj4OTbHQIZrfyzfe6APYFH3ix9EBh3UqLF3lm7ei_nBIrj5mZ0l4x5yNxFeUT01Tq3oW-prtnqxqZl9CyzxqA")

        albumRecyclerViewAdapter = AlbumRecyclerViewAdapter(context!!, dataList)
        rv_album_container.adapter = albumRecyclerViewAdapter
        rv_album_container.layoutManager = GridLayoutManager(context!!, 3)
    }

}
