package com.example.Capstone.activities

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.HashtagRecyclerViewAdapter
import com.example.Capstone.adapter.MemoRecyclerViewAdapter
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.data.TagData
import com.example.Capstone.network.get.GetSpecificScrapResponse
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.toolbar_with_trashbin.*
import kotlinx.android.synthetic.main.dialog_add_memo.*
import kotlinx.android.synthetic.main.dialog_modify_title.*
import org.jetbrains.anko.lines
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InformationActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    //problem : instagram만 click이 막아지지 않음
    private var saved_url = ""
//    private val saved_url = "https://www.youtube.com/embed/" + "zwjZ-ERMp1k"

    lateinit var memoRecyclerViewAdapter: MemoRecyclerViewAdapter
    lateinit var hashTagRecyclerViewAdapter: HashtagRecyclerViewAdapter

    var memoList: ArrayList<String> = ArrayList()
    var hashtagList : ArrayList<String> = ArrayList()

    private var scrapId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        scrapId = intent.getIntExtra("id", 0)
        getSpecificScrapResponse(scrapId)

        //get web view settings instance
        val settings = web_url.settings

        //enable java script in web view
        settings.javaScriptEnabled = true

        //enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.setAppCachePath(cacheDir.path)

        //enable zoom
        settings.setSupportZoom(true)
        //settings.builtInZoomControls = true
        //settings.displayZoomControls = true
        settings.textZoom = 125

        //enable disable images
        settings.blockNetworkImage = false
        settings.loadsImagesAutomatically = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            settings.safeBrowsingEnabled = true //api 26
        }

        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false

        web_url.webViewClient = object: WebViewClient(){
            //blocking move to any other url
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = Uri.parse(view?.url)
                if (uri.toString() == (saved_url)) { //youtube일때는 이동 허용해주는 코드 필요
                    // This is my web site, so do not override; let my WebView load the page
                    return false
                }
                toast("cannot move to another page")
                return true
            }

            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return true
            }
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_trashbin.setOnClickListener {
            showDeleteDialog(null)
        }

        btn_add_memo.setOnClickListener {
            showMemoDialog()
        }

        info_title.setOnLongClickListener{
            showTitleDialog()
            true
        }

        memoRecyclerViewAdapter = MemoRecyclerViewAdapter(this, memoList)
        rv_memo_container.adapter = memoRecyclerViewAdapter
        rv_memo_container.layoutManager = LinearLayoutManager(this)

        hashTagRecyclerViewAdapter = HashtagRecyclerViewAdapter(this, hashtagList, true)
        rv_hashtag_container.adapter = hashTagRecyclerViewAdapter
        rv_hashtag_container.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        btn_change_menu.setOnClickListener {
            var menu : PopupMenu = PopupMenu(this, btn_change_menu)
            menu.menu.add("전체")
            menu.menu.add("맛집")
            menu.menu.add("먹거리")
            menu.show()
        }

        btn_add_hashtag.setOnClickListener {
            showHashtagDialog()
        }

//        for (ht in hashtagTextList){
//            ht.setOnLongClickListener{
//                showDeleteDialog(ht)
//                true
//            }
//        }
    }

    private fun getSpecificScrapResponse(id: Int){
        val getAllScrapListResponse = networkService.getSpecificScrapResponse(id)

        getAllScrapListResponse.enqueue(object : Callback<GetSpecificScrapResponse> {

            override fun onFailure(call: Call<GetSpecificScrapResponse>, t: Throwable) {
                Log.e("get scrap content failed", t.toString())
            }

            override fun onResponse(
                call: Call<GetSpecificScrapResponse>,
                response: Response<GetSpecificScrapResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("babo", response.body().toString())

                    val data: GetSpecificScrapResponse? = response.body()

                    if (data != null) {
                        info_title.text = data.title
                        saved_url = data.url
                        info_date.text = data.date.substring(0,10)

                        hashtagList.clear()
                        if (data.tags != null) {
                            for (tag in data.tags) {
                                hashtagList.add(tag.tag_text)
                            }
                        }

                        memoList.clear()
                        if (data.memos != null) {
                            for (memo in data.memos) {
                                memoList.add(memo.memo)
                            }
                        }

                        hashTagRecyclerViewAdapter.notifyDataSetChanged()
                        memoRecyclerViewAdapter.notifyDataSetChanged()

                        web_url.loadUrl(saved_url)

                    }

                }

                else{
                    Log.e("error", "fail")
                }
            }
        })
    }

    private fun addMemo(memo : String){
        memoList.add(memo)
        memoRecyclerViewAdapter.notifyDataSetChanged()
    }
    private fun showMemoDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_memo)

        val memo = dialog.findViewById(R.id.edt_memo) as EditText
        val submit = dialog.findViewById(R.id.btn_submit_memo) as TextView
        submit.setOnClickListener {
            dialog.dismiss()
            if (memo.text.toString().isNotEmpty()){
                addMemo(memo.text.toString())
            }
        }
        dialog.show()
    }

    private fun showDeleteDialog(view : TextView?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_delete)

        val delete = dialog.findViewById(R.id.btn_delete_memo) as TextView
        delete.setOnClickListener {
            dialog.dismiss()
            if (view != null) {
                view.visibility = View.GONE
                //delete hashtag request
            }
            else{
                finish()
                //delete information request
            }
        }
        dialog.show()
    }

    private fun showTitleDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_modify_title)

        val title = dialog.findViewById(R.id.edt_title) as EditText
        val submit = dialog.findViewById(R.id.btn_submit_title) as TextView
        submit.setOnClickListener {
            dialog.dismiss()
            if (title.toString().isNotEmpty()){
                info_title.text = title.text.toString()
            }
        }
        dialog.show()
    }
    private fun showHashtagDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_modify_title)

        val submit = dialog.findViewById(R.id.btn_submit_title) as TextView
        submit.text = "해시태그 추가"

        val category = dialog.findViewById(R.id.dialog_category) as TextView
        category.text = "hashtag"

        val hashtag = dialog.findViewById(R.id.edt_title) as EditText
        hashtag.lines = 1
        hashtag.hint = "해시태그를 입력해주세요"

        submit.setOnClickListener {
            dialog.dismiss()
            if (hashtag.text.toString().isNotEmpty()){
//                addHashtag(hashtag.text.toString())
            }
        }
        dialog.show()
    }
//    private fun addHashtag(str: String) {
//        var tempHashtag = hashtagTextList[hashtagSize]
//        tempHashtag.visibility = View.VISIBLE
//        tempHashtag.text = "#" + str
//        hashtagSize++
//    }
}
