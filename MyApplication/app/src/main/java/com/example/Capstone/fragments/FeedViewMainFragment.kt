package com.example.Capstone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.FeedRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Feed
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllScrapListResponse
import kotlinx.android.synthetic.main.fragment_feed_view_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class FeedViewMainFragment : Fragment() {

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private var dataList : ArrayList<Feed> = ArrayList()
    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_view_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getAllScrapListResponse(SharedPreferenceController.getUserId(context!!)!!)
        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(context!!, dataList)
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(context!!)
    }

    fun changeRecyclerViewData(charSequence: CharSequence){
        feedRecyclerViewAdapter.filter.filter(charSequence)
        feedRecyclerViewAdapter.notifyDataSetChanged()
    }
    private fun updateDataList(list: ArrayList<Feed>){
        dataList.clear()
        dataList.addAll(list)
        feedRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun getAllScrapListResponse(id: Int){
        val getAllScrapListResponse = networkService.getAllScrapListResponse(id)

        getAllScrapListResponse.enqueue(object : Callback<ArrayList<GetAllScrapListResponse>> {

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
                    val tempDataList : ArrayList<Feed> = ArrayList()

                    if (data != null) {
                        for(scrap in data) {
                            tempDataList.add(scrap.toFeedDetail())
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