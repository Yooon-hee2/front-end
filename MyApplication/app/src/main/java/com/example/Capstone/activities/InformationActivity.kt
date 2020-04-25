package com.example.Capstone.activities

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.MemoRecyclerViewAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.toolbar_with_trashbin.*
import kotlinx.android.synthetic.main.dialog_add_memo.*
import kotlinx.android.synthetic.main.dialog_modify_title.*
import org.jetbrains.anko.lines
import org.jetbrains.anko.toast
import org.w3c.dom.Text


class InformationActivity : AppCompatActivity() {

    //problem : instagram만 click이 막아지지 않음
    //private val saved_url = "https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin"
    private val saved_url = "https://www.youtube.com/embed/" + "zwjZ-ERMp1k"
    lateinit var memoRecyclerViewAdapter: MemoRecyclerViewAdapter
    var memoList: ArrayList<String> = ArrayList()

    val hashtagList : ArrayList<String>? = ArrayList()
    var hashtagSize = 0

    private lateinit var hashtag1: TextView
    private lateinit var hashtag2: TextView
    private lateinit var hashtag3: TextView
    private lateinit var hashtag4: TextView
    private lateinit var hashtag5: TextView
    private lateinit var hashtagTextList: ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        hashtag1 = findViewById(R.id.info_hashtag1)
        hashtag2 = findViewById(R.id.info_hashtag2)
        hashtag3 = findViewById(R.id.info_hashtag3)
        hashtag4 = findViewById(R.id.info_hashtag4)
        hashtag5 = findViewById(R.id.info_hashtag5)
        hashtagTextList = arrayListOf(hashtag1, hashtag2, hashtag3, hashtag4, hashtag5)


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

        web_url.loadUrl(saved_url)

        web_url.webViewClient = object: WebViewClient(){
            //blocking move to any other url
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = Uri.parse(view?.url)
                if (uri.equals(saved_url)) { //youtube일때는 이동 허용해주는 코드 필요
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

        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

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

        memoList.add("이전에 추가했던 메모입니다")

        memoRecyclerViewAdapter = MemoRecyclerViewAdapter(this, memoList)
        rv_memo_container.adapter = memoRecyclerViewAdapter
        rv_memo_container.layoutManager = LinearLayoutManager(this)

        btn_change_menu.setOnClickListener {
            var menu : PopupMenu = PopupMenu(this, btn_change_menu)
            menu.menu.add("전체")
            menu.menu.add("맛집")
            menu.menu.add("먹거리")
            menu.show();
        }

        btn_add_hashtag.setOnClickListener {
            showHashtagDialog()
        }

        hashtagList?.add("애플")
        hashtagList?.add( "사랑해")
        hashtagList?.add("귀여워")

        if (hashtagList != null) {
            for (item in hashtagList){
                hashtagTextList[hashtagSize].visibility = View.VISIBLE
                hashtagTextList[hashtagSize].text = "#" + item
                hashtagSize++
            }
        }

        for (ht in hashtagTextList){
            ht.setOnLongClickListener{
                showDeleteDialog(ht)
                true
            }
        }
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
                addHashtag(hashtag.text.toString())
            }
        }
        dialog.show()
    }
    private fun addHashtag(str: String) {
        var tempHashtag = hashtagTextList[hashtagSize]
        tempHashtag.visibility = View.VISIBLE
        tempHashtag.text = "#" + str
        hashtagSize++
    }
}
