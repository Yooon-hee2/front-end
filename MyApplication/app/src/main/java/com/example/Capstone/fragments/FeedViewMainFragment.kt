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
import com.example.Capstone.network.get.GetFolderScrapListResponse
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

    private var userId : Int = -1
    private var currFolderId = 0
    private var dataList : ArrayList<Feed> = ArrayList()
    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedRecyclerViewAdapter = FeedRecyclerViewAdapter(context!!, dataList)
        userId = SharedPreferenceController.getCurrentUserId(context!!)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        getAllScrapListResponse(userId)
        return inflater.inflate(R.layout.fragment_feed_view_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userId = SharedPreferenceController.getCurrentUserId(context!!)!!
        rv_feed_container.adapter = feedRecyclerViewAdapter
        rv_feed_container.layoutManager = LinearLayoutManager(context!!)
        getAllScrapListResponse(userId)
        if (!SharedPreferenceController.getUserFolderInfo(context!!).isNullOrEmpty()){
            currFolderId = SharedPreferenceController.getUserFolderInfo(context!!)["전체"]!!
        }

        swipe_refresh_feed.setOnRefreshListener {
//            dataList.clear()
            feedRecyclerViewAdapter.notifyDataSetChanged()
            when(currFolderId){
                SharedPreferenceController.getUserFolderInfo(context!!)["전체"]!! -> getAllScrapListResponse(userId)
                else -> getAllFolderScrapListResponse(userId, currFolderId)
            }
            swipe_refresh_feed.setRefreshing(false)
        }
    }

    override fun onResume() {
        super.onResume()
        userId = SharedPreferenceController.getCurrentUserId(context!!)!!
//        getAllScrapListResponse(userId)
    }

    fun changeRecyclerViewData(charSequence: CharSequence){
        feedRecyclerViewAdapter.filter.filter(charSequence)
        feedRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun changeFolder(folderId: Int){
        currFolderId = folderId
        if (userId != -1) {
            when(folderId){
                SharedPreferenceController.getUserFolderInfo(context!!)["전체"]!! -> getAllScrapListResponse(userId)
                else -> getAllFolderScrapListResponse(userId, folderId)
            }
        }
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
                    val data: ArrayList<GetAllScrapListResponse>? = response.body() //temp가 없을 때 터짐
                    val tempDataList : ArrayList<Feed> = ArrayList()
                    Log.d("success", response.body().toString())
                    if (!data.isNullOrEmpty()) {
                        for(scrap in data) {
                            tempDataList.add(scrap.toFeedDetail())
                        }
                        feedRecyclerViewAdapter.calcDiff(tempDataList)
                    }
                }

                else{
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun getAllFolderScrapListResponse(id: Int, folderId : Int){
        val getAllFolderScrapListResponse = networkService.getFolderScrapListResponse(id, folderId)

        getAllFolderScrapListResponse.enqueue(object : Callback<ArrayList<GetFolderScrapListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetFolderScrapListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetFolderScrapListResponse>>,
                response: Response<ArrayList<GetFolderScrapListResponse>>
            ) {
                if(response.isSuccessful){
                    val data: ArrayList<GetFolderScrapListResponse>? = response.body() //temp가 없을 때 터짐
                    Log.d("good", response.body().toString())
                    val tempDataList : ArrayList<Feed> = ArrayList()
                    for(folder in data!!) {
                        if (!folder.scraps.isNullOrEmpty()){
                            for (scrap in folder.scraps!!) {
                                tempDataList.add(scrap.toFeedDetail())
                            }
                            feedRecyclerViewAdapter.calcDiff(tempDataList)
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