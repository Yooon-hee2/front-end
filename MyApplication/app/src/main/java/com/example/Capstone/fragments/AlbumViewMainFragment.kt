package com.example.Capstone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.AlbumRecyclerViewAdapter
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Feed
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllScrapListResponse
import com.example.Capstone.network.get.GetFolderScrapListResponse
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
    private var userId : Int = -1
    private var dataList : ArrayList<Feed> = ArrayList()
    lateinit var albumRecyclerViewAdapter: AlbumRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        albumRecyclerViewAdapter = AlbumRecyclerViewAdapter(context!!, dataList)
        userId = SharedPreferenceController.getUserId(context!!)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getAllScrapListResponse(userId)
        return inflater.inflate(R.layout.fragment_album_view_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getAllScrapListResponse(userId)
        rv_album_container.adapter = albumRecyclerViewAdapter
        rv_album_container.layoutManager = GridLayoutManager(context!!, 3)
    }

    fun changeFolder(folderId: Int){
        if (userId != -1) {
            when(folderId){
                2 -> getAllScrapListResponse(userId)
                else -> getAllFolderScrapListResponse(userId, folderId)
            }
        }
    }

    fun changeRecyclerViewData(charSequence: CharSequence){
        albumRecyclerViewAdapter.filter.filter(charSequence)
        albumRecyclerViewAdapter.notifyDataSetChanged()
    }


    private fun updateDataList(list: ArrayList<Feed>){
        dataList.clear()
        dataList.addAll(list)
        albumRecyclerViewAdapter.notifyDataSetChanged()
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
                    Log.d("success", response.body().toString())
                    val data: ArrayList<GetFolderScrapListResponse>? = response.body() //temp가 없을 때 터짐
                    val tempDataList : ArrayList<Feed> = ArrayList()

                    if (data != null) {
                        for(folder in data) {
                            for (scrap in folder.scraps!!) {
                                tempDataList.add(scrap.toFeedDetail())
                                updateDataList(tempDataList)
                            }
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
