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
    private var idList : ArrayList<Int> = ArrayList()

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

        getAllScrapUrlListResponse(SharedPreferenceController.getUserId(context!!)!!)
        albumRecyclerViewAdapter = AlbumRecyclerViewAdapter(context!!, dataList, idList)
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
                    val data: ArrayList<GetAllScrapListResponse>? = response.body() //temp가 없을 때 터짐
                    val tempDataList : ArrayList<String> = ArrayList()

                    if (data != null) {
                        for(scrap in data) {
                            tempDataList.add(scrap.thumbnail)
                            updateDataList(tempDataList)
                            idList.add(scrap.scrap_id)
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
