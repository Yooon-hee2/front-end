package com.example.Capstone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.Capstone.R
import com.example.Capstone.activities.MainActivity
import java.util.*
import kotlin.collections.ArrayList

class SearchListViewAdapter (mContext: Context, recommendedHashtagList : ArrayList<String>) : BaseAdapter() {
    private var inflater: LayoutInflater = LayoutInflater.from(mContext)
    private val recommendedHashtagList : ArrayList<String> = recommendedHashtagList
    private val  filteredHashtagList: ArrayList<String> = ArrayList()

    init {
        this.filteredHashtagList.addAll(recommendedHashtagList)
        Log.d("hashtag", this.filteredHashtagList.toString())
    }

    inner class ViewHolder {
        internal var hashtag: TextView? = null
        lateinit var editText : EditText
        lateinit var li_hashtag_container : RelativeLayout
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.list_item_hashtag, parent, false)
            // Locate the TextViews in listview_item.xml
            holder.hashtag = view!!.findViewById(R.id.hashtag_name) as TextView
            holder.li_hashtag_container = view!!.findViewById(R.id.li_item_hashtag_container)
            holder.editText = MainActivity.edt_search
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        // Set the results into TextViews
        holder.hashtag!!.text = filteredHashtagList[position]

        // when user click listview item, set text on edittext
        holder.li_hashtag_container!!.setOnClickListener {
            holder.editText.setText(holder.hashtag!!.text)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return filteredHashtagList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredHashtagList.size
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        filteredHashtagList.clear()
        if (charText.isEmpty()) {
            filteredHashtagList.addAll(recommendedHashtagList)
        }
        else {
            for (ht in filteredHashtagList) {
                if (ht.substring(1, ht.length).toLowerCase().contains(charText)) {
                    filteredHashtagList.add(ht)
                }
            }
        }
        notifyDataSetChanged()
    }
}
