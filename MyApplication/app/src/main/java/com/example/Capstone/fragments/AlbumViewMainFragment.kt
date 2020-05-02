package com.example.Capstone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.AlbumRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllScrapListResponse
import kotlinx.android.synthetic.main.fragment_album_view_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AlbumViewMainFragment : Fragment() {

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private var dataList : ArrayList<String> = ArrayList()

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

//        var dataList: ArrayList<String> = ArrayList()
//
//        dataList.add("https://t1.daumcdn.net/cfile/tistory/244CE24556B5642E36")
//        dataList.add("http://blogfiles.naver.net/MjAxNjEwMzBfMTU5/MDAxNDc3ODEzNTAyNzc5.LcGMoEbTvDQ6VSw2VPg8DmWCKFLNgMjVwDsKT9287iUg.vjmV-" +
//                "W8lxd4uzMBY3A0ZwRU3NiSHxZ-C0__kprqbZa8g.JPEG.moonfrost/DSC04123.JPG")
//        dataList.add("https://www.topstarnews.net/news/photo/201805/415426_62309_4431.jpg")
//        dataList.add("https://lh3.googleusercontent.com/proxy/6dRaCzOlQZtZ52zySWmKPGcA0sWYOtw9tswZbdakgWLqB_D6B_" +
//                "-Tj4OTbHQIZrfyzfe6APYFH3ix9EBh3UqLF3lm7ei_nBIrj5mZ0l4x5yNxFeUT01Tq3oW-prtnqxqZl9CyzxqA")

        getAllScrapUrlListResponse(SharedPreferenceController.getUserId(context!!)!!)
        albumRecyclerViewAdapter = AlbumRecyclerViewAdapter(context!!, dataList)
        rv_album_container.adapter = albumRecyclerViewAdapter
        rv_album_container.layoutManager = GridLayoutManager(context!!, 3)
    }

    private fun updateDataList(list: ArrayList<String>){
        dataList.clear()
        dataList.addAll(list)
        albumRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun getAllScrapUrlListResponse(id: Int){
        val getAllScrapUrlListResponse = networkService.getAllScrapListResponse(id)

        getAllScrapUrlListResponse.enqueue(object : Callback<ArrayList<GetAllScrapListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetAllScrapListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetAllScrapListResponse>>,
                response: Response<ArrayList<GetAllScrapListResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("babo", response.body().toString())

                    val data: ArrayList<GetAllScrapListResponse>? = response.body() //temp가 없을 때 터짐
                    val tempDataList : ArrayList<String> = ArrayList()

                    if (data != null) {
                        for(scrap in data) {
                            tempDataList.add(scrap.thumbnail)
                            updateDataList(tempDataList)
                        }
                    }
                }

                else{
                    Log.e("error", "fail")
                }
            }
        })
    }
}
