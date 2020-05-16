package com.example.Capstone.adapter

import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.Capstone.fragments.AlbumViewMainFragment
import com.example.Capstone.fragments.FeedViewMainFragment
import org.jetbrains.anko.toast

class MainFragmentAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm) {

    companion object { //싱글톤 design pattern ( 기존에 생성된 객체 재사용, 그때그때마다 새롭게 생성 방지 )

        private var feedFragment: FeedViewMainFragment? = null
        private var albumFragment: AlbumViewMainFragment? = null

        @Synchronized
        fun getFeedFragment() : FeedViewMainFragment{
            if(feedFragment == null) feedFragment = FeedViewMainFragment()
            return feedFragment!!
        }

        @Synchronized
        fun getAlbumFragment() : AlbumViewMainFragment{
            if(albumFragment == null) albumFragment = AlbumViewMainFragment()
            return albumFragment!!
        }

    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> getFeedFragment()
            1 -> getAlbumFragment()
            else -> getFeedFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    fun getEditText(charSequence: CharSequence){
        getFeedFragment().changeRecyclerViewData(charSequence)
        getAlbumFragment().changeRecyclerViewData(charSequence)
    }

    fun changeFolder(folderId : Int) {
        getFeedFragment().changeFolder(folderId)
        getAlbumFragment().changeFolder(folderId)
    }
}