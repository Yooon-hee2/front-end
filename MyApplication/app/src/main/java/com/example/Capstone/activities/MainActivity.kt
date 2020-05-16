package com.example.Capstone.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.example.Capstone.R
import com.example.Capstone.adapter.MainFragmentAdapter
import com.example.Capstone.adapter.SearchListViewAdapter
import com.example.Capstone.background.JobScheduler
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Feed
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllFolderListResponse
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.nav_drawer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(){

    lateinit var pager : ViewPager

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    companion object{
        val recommendedHashtagList = arrayListOf("#강남", "#이태원", "#플레이리스트", "#맛집", "#동물의숲")
        lateinit var edt_search : EditText
        var folderList : HashMap<String, Int> = HashMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JobScheduler.start(this)

        edt_search = findViewById(R.id.search_item)

        folder_name.text = "전체"
        var dataList: ArrayList<Feed> = ArrayList()
        pager = findViewById(R.id.vp_main)
        val pagerAdapter = MainFragmentAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter

        val tabLayout : TabLayout = findViewById(R.id.tl_main)
        val listView = findViewById<ListView>(R.id.list_search_item)


        val searchListCustomAdapter = SearchListViewAdapter(this)

        listView.adapter = searchListCustomAdapter


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_feed))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_album))

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        btn_hamburger.setOnClickListener {
            if (!ly_drawer.isDrawerOpen(GravityCompat.END)) {
                ly_drawer.openDrawer(GravityCompat.END)
            }
        }

        switch_noti.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                switch_noti.setTrackResource(R.drawable.switch_track_on)
            }
            else{
                switch_noti.setTrackResource(R.drawable.switch_track_off)
            }
        }

        btn_search.setOnClickListener {
            pagerAdapter.getEditText(search_item.text)
            list_search_item.visibility = View.GONE
            tab_main.visibility = View.VISIBLE
        }

        btn_erase_all.setOnClickListener {
            search_item.text.clear()
            pagerAdapter.getEditText(search_item.text)
        }

        search_item.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (search_item.text.toString().isNotEmpty()){
                    btn_erase_all.visibility = View.VISIBLE
                    if(search_item.text.toString().substring(0,1) == "#") {
                        list_search_item.visibility = View.VISIBLE
                        tab_main.visibility = View.GONE
                        search_item.setTextColor(resources.getColor(R.color.red))
                    }
                    else{
                        search_item.setTextColor(resources.getColor(R.color.black))
                    }
                }
                else{
                    tab_main.visibility = View.VISIBLE
                    list_search_item.visibility = View.GONE
                    btn_erase_all.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                pagerAdapter.getEditText(search_item.text)
                if (charSequence!!.isNotBlank()) {
                    if(charSequence.toString().substring(0,1) == "#") {
                        searchListCustomAdapter.filter(charSequence.substring(1, charSequence.length))
                    }
                }
            }
        })

        btn_personal_storage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_share_storage.setOnClickListener {
            val intent = Intent(this, StorageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_manage_folder.setOnClickListener {
            val intent = Intent(this, FolderManageActivity::class.java)
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_manage_storage.setOnClickListener {
            val intent = Intent(this, StorageManageActivity::class.java)
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_cancel.setOnClickListener {
            if (ly_drawer.isDrawerOpen(GravityCompat.END)) {
                ly_drawer.closeDrawer(GravityCompat.END)
            }
        }

        getAllFolderListResponse(SharedPreferenceController.getUserId(this)!!)

        val button = findViewById<ImageView>(R.id.folder_menu)
        button.setOnClickListener {
            val popupMenu = PopupMenu(this, button)
            for (folder in folderList.keys){
                popupMenu.menu.add(folder)
            }
            popupMenu.setOnMenuItemClickListener { item ->
                folder_name.text = item.title
                folderList[item.title]?.let { it1 -> pagerAdapter.changeFolder(it1) }
                true
            }
            popupMenu.show()
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun getAllFolderListResponse(id: Int){ //programId 넘겨주기
        val getAllFolderListResponse = networkService.getAllFolderListResponse(id)

        getAllFolderListResponse.enqueue(object : Callback<ArrayList<GetAllFolderListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetAllFolderListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetAllFolderListResponse>>,
                response: Response<ArrayList<GetAllFolderListResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("babo", response.body().toString())

                    val data: ArrayList<GetAllFolderListResponse>? = response.body()
                    if (data != null) {
                        for(folders in data) {
                            for(folder in folders.folders){
                                Log.d("dddddddddd", folder.folder_id.toString())
                                if(folder.folder_key == 0){
                                    folderList["전체"] = folder.folder_id
                                }
                                else{
                                    folderList[folder.folder_name] = folder.folder_id
                                }
                            }
                        }
                        SharedPreferenceController.setUserFolderInfo(this@MainActivity, folderList)
                    }
                }
                else{
                    Log.e("error", "fail")
                }
            }
        })
    }
}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            Log.e("isServiceRunning", "Service is running")
            return true
        }
    }
    return false
}