package com.example.Capstone.adapter

import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.Capstone.fragments.AlbumViewMainFragment
import com.example.Capstone.fragments.FeedViewMainFragment
import org.jetbrains.anko.toast

class MainFragmentAdapter: FragmentPagerAdapter {

    private var fragmentList : ArrayList<Fragment> = ArrayList()

    constructor(fragmentManager: FragmentManager) : super(fragmentManager) {
        fragmentList.add(FeedViewMainFragment())
        fragmentList.add(AlbumViewMainFragment())
    }
//
//    companion object { //싱글톤 design pattern ( 기존에 생성된 객체 재사용, 그때그때마다 새롭게 생성 방지 )
//
//        private var feedFragment: FeedViewMainFragment? = null
//        private var albumFragment: AlbumViewMainFragment? = null
//
//        @Synchronized
//        fun getFeedFragment() : FeedViewMainFragment{
//            if(feedFragment == null) feedFragment = FeedViewMainFragment()
//            return feedFragment!!
//        }
//
//        @Synchronized
//        fun getAlbumFragment() : AlbumViewMainFragment{
//            if(albumFragment == null) albumFragment = AlbumViewMainFragment()
//            return albumFragment!!
//        }
//
//    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return 2
    }

    fun getEditText(charSequence: CharSequence){
        FeedViewMainFragment().changeRecyclerViewData(charSequence)
        AlbumViewMainFragment().changeRecyclerViewData(charSequence)
    }

    fun changeFolder(folderId : Int) {
        FeedViewMainFragment().changeFolder(folderId)
        AlbumViewMainFragment().changeFolder(folderId)
    }
}