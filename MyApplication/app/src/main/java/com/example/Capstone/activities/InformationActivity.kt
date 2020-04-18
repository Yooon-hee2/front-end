package com.example.Capstone.activities

import android.app.Dialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Capstone.R
import com.example.Capstone.adapter.MemoRecyclerViewAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.toolbar_with_trashbin.*
import kotlinx.android.synthetic.main.dialog_add_memo.*
import org.jetbrains.anko.toast


class InformationActivity : AppCompatActivity() {

    //problem : instagram만 click이 막아지지 않음
    private val saved_url = "https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin"
    lateinit var memoRecyclerViewAdapter: MemoRecyclerViewAdapter
    var memoList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)


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
                if (uri.equals(saved_url)) {
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

        btn_add_memo.setOnClickListener {
            showDialog()
        }

        memoList.add("이전에 추가했던 메모입니다")

        memoRecyclerViewAdapter = MemoRecyclerViewAdapter(this, memoList)
        rv_memo_container.adapter = memoRecyclerViewAdapter
        rv_memo_container.layoutManager = LinearLayoutManager(this)
    }

    private fun addMemo(memo : String){
        memoList.add(memo)
        memoRecyclerViewAdapter.notifyDataSetChanged()
    }
    private fun showDialog() {
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
}
