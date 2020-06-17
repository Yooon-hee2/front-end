package com.example.Capstone.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.Capstone.R
import com.example.Capstone.adapter.MainFragmentAdapter
import com.example.Capstone.adapter.SearchListViewAdapter
import com.example.Capstone.background.AlarmBroadcastReceiver
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.fragments.AlbumViewMainFragment
import com.example.Capstone.fragments.FeedViewMainFragment
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.get.GetAllFolderListResponse
import com.example.Capstone.network.get.GetAllStorageListResponse
import com.example.Capstone.network.get.GetRandomTagListResponse
import com.example.Capstone.network.post.PostLocationAlarmisNullResponse
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.nav_drawer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(){

    lateinit var pager : ViewPager

    lateinit var feedFragment : Fragment
    lateinit var albumFragment : Fragment

    lateinit var searchListCustomAdapter : SearchListViewAdapter

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val LOCATION_REQUEST_CODE = 1000
    var recommendedHashtagList : ArrayList<String> = ArrayList()
    var currFolderId = 0

    companion object{
        lateinit var edt_search : EditText
        var folderList : HashMap<String, Int> = HashMap()
        var storageList : HashMap<String, Int> = HashMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllFolderListResponse(SharedPreferenceController.getCurrentUserId(this)!!)
        getAllStorageListResponse(SharedPreferenceController.getUserId(this)!!)
        getRandomTagListResponse(SharedPreferenceController.getCurrentUserId(this)!!)
        edt_search = findViewById(R.id.search_item)

        folder_name.text = "전체"
        tv_drawer_email.text = SharedPreferenceController.getUserEmail(this)
        tv_drawer_nickname.text = SharedPreferenceController.getUserNickname(this)
        pager = findViewById(R.id.vp_main)
        val pagerAdapter = MainFragmentAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter

        feedFragment = pagerAdapter.getItem(0)
        albumFragment = pagerAdapter.getItem(1)

        val tabLayout : TabLayout = findViewById(R.id.tl_main)
        val listView = findViewById<ListView>(R.id.list_search_item)

        searchListCustomAdapter = SearchListViewAdapter(this, recommendedHashtagList)

        listView.adapter = searchListCustomAdapter

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_feed))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_album))

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        if (SharedPreferenceController.getUserNoti(this)!!){
            switch_noti.setTrackResource(R.drawable.switch_track_on)
            switch_noti.isChecked = true
        }
        else{
            switch_noti.setTrackResource(R.drawable.switch_track_off)
            switch_noti.isChecked = false
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("location", "no permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
        }

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
                SharedPreferenceController.setUserNoti(this, true)
                switch_noti.setTrackResource(R.drawable.switch_track_on)
            }
            else{
                SharedPreferenceController.setUserNoti(this, false)
                switch_noti.setTrackResource(R.drawable.switch_track_off)
            }
        }

        btn_search.setOnClickListener {
            (feedFragment as FeedViewMainFragment).changeRecyclerViewData(search_item.text)
            (albumFragment as AlbumViewMainFragment).changeRecyclerViewData(search_item.text)
//            pagerAdapter.getEditText(search_item.text)
            list_search_item.visibility = View.GONE
            tab_main.visibility = View.VISIBLE
        }

        btn_erase_all.setOnClickListener {
            search_item.text.clear()
//            pagerAdapter.getEditText(search_item.text)
            (feedFragment as FeedViewMainFragment).changeRecyclerViewData(search_item.text)
            (albumFragment as AlbumViewMainFragment).changeRecyclerViewData(search_item.text)
        }

        search_item.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
//                pagerAdapter.getEditText(search_item.text)
                (feedFragment as FeedViewMainFragment).changeRecyclerViewData(search_item.text)
                (albumFragment as AlbumViewMainFragment).changeRecyclerViewData(search_item.text)
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
                if (charSequence!!.isNotBlank()) {
//                    pagerAdapter.getEditText(search_item.text)
                    (feedFragment as FeedViewMainFragment).changeRecyclerViewData(search_item.text)
                    (albumFragment as AlbumViewMainFragment).changeRecyclerViewData(search_item.text)
                    if(charSequence.toString().substring(0,1) == "#") {
                        searchListCustomAdapter.filter(charSequence.substring(1, charSequence.length))
                    }
                }
            }
        })

        iv_drawer_profileimg.setOnClickListener{
            getRecrawlingResponse(SharedPreferenceController.getCurrentUserId(this)!!)
        }

        btn_personal_storage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            val userPrivateId = SharedPreferenceController.getUserId(this)
            SharedPreferenceController.setCurrentUserId(this, userPrivateId!!)
            startActivity(intent)
        }

        btn_share_storage.setOnClickListener {
            val intent = Intent(this, StorageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ly_drawer.closeDrawer(GravityCompat.END)
            startActivity(intent)
        }

        btn_temp_test.setOnClickListener {
            if(SharedPreferenceController.getUserNoti(this)!!){
                val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
                intent.putExtra("id", 141)
                applicationContext.sendBroadcast(intent)
            }
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

        btn_logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            ly_drawer.closeDrawer(GravityCompat.END)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            SharedPreferenceController.clearUserSharedPreferences(this)
            startActivity(intent)
        }

        val button = findViewById<ImageView>(R.id.folder_menu_main)
        button.setOnClickListener {
            val popupMenu = PopupMenu(this, button)
            for (folder in folderList.keys){
                popupMenu.menu.add(folder)
            }
            popupMenu.setOnMenuItemClickListener { item ->
                folder_name.text = item.title
                folderList[item.title]?.let {
                        it1 ->
                    (feedFragment as FeedViewMainFragment).changeFolder(it1)
                    (albumFragment as AlbumViewMainFragment).changeFolder(it1)
                    currFolderId = it1
                }
                true
            }
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
        search_item.text.clear()
        getAllFolderListResponse(SharedPreferenceController.getCurrentUserId(this)!!)
        getAllStorageListResponse(SharedPreferenceController.getUserId(this)!!)
        var currFolderId = SharedPreferenceController.getUserFolderInfo(this!!)[folder_name.text]
        if (currFolderId != null) {
            (feedFragment as FeedViewMainFragment).changeFolder(currFolderId)
            (albumFragment as AlbumViewMainFragment).changeFolder(currFolderId)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun getAllFolderListResponse(id: Int){
        folderList.clear()

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

    private fun getAllStorageListResponse(id: Int){
        storageList.clear()
        val getAllStorageListResponse = networkService.getAllStorageListResponse("application/json", id)

        getAllStorageListResponse.enqueue(object : Callback<ArrayList<GetAllStorageListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetAllStorageListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetAllStorageListResponse>>,
                response: Response<ArrayList<GetAllStorageListResponse>>
            ) {
                if(response.isSuccessful){
                    storageList[SharedPreferenceController.getUserNickname(this@MainActivity) + "의 저장소"] = SharedPreferenceController.getUserId(this@MainActivity)!!.toInt()
                    Log.d("babo", response.body().toString())
                    val data: ArrayList<GetAllStorageListResponse>? = response.body()
                    if (data != null) {
                        for(storage in data) {
                            storageList[storage.sharing_name] = storage.id.toInt()
                        }
                    }
                    SharedPreferenceController.setUserStorageInfo(this@MainActivity, storageList)
                }
                else{
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun getRandomTagListResponse(id: Int){
        recommendedHashtagList.clear()

        val getRandomTagListResponse = networkService.getRandomTagListResponse("application/json", id)

        getRandomTagListResponse.enqueue(object : Callback<ArrayList<GetRandomTagListResponse>> {

            override fun onFailure(call: Call<ArrayList<GetRandomTagListResponse>>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<GetRandomTagListResponse>>,
                response: Response<ArrayList<GetRandomTagListResponse>>
            ) {
                if(response.isSuccessful){
                    val data: ArrayList<GetRandomTagListResponse>? = response.body()
                    if (!data.isNullOrEmpty()) {
                        for(tag in data) {
                            Log.d("randomTagList", tag.tag_text)
                            recommendedHashtagList.add(tag.tag_text)
                        }
                    }
                    searchListCustomAdapter.notifyDataSetChanged()
                }
                else{
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun getRecrawlingResponse(id: Int){
        val getRecrawlingResponse = networkService.getRecrawlResponse("application/json", id)

        getRecrawlingResponse.enqueue(object : Callback<PostLocationAlarmisNullResponse> {

            override fun onFailure(call: Call<PostLocationAlarmisNullResponse>, t: Throwable) {
                Log.e("get List failed", t.toString())
            }

            override fun onResponse(
                call: Call<PostLocationAlarmisNullResponse>,
                response: Response<PostLocationAlarmisNullResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("success", "success")
                }
                else{
                    Log.e("error", "fail")
                }
            }
        })
    }
}
