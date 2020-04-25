package com.example.Capstone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Capstone.R
import com.example.Capstone.activities.InformationActivity
import com.example.Capstone.model.Feed
import org.jetbrains.anko.*

class FeedRecyclerViewAdapter(val ctx: Context, var list: ArrayList<Feed>)  :

    RecyclerView.Adapter<FeedRecyclerViewAdapter.Holder>(), Filterable {

    private var filteredList: ArrayList<Feed>? = null

    init {
        this.filteredList = list
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
            val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_feed, viewGroup, false)
            return Holder(view)
        }

    override fun getItemCount(): Int = filteredList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = filteredList!![position].title

        if (filteredList!![position].src == "instagram"){
            holder.src.setImageResource(R.drawable.ic_instagram)
        }
        else{
            holder.src.setImageResource(R.drawable.ic_facebook)
        }

        if (URLUtil.isValidUrl(filteredList!![position].thumbnail)) {
            Glide.with(ctx).load(filteredList!![position].thumbnail).into(holder.thumbnail)
        }

        holder.container.setOnClickListener {
            ctx.startActivity<InformationActivity>()
        }
        val hashtagList : ArrayList<String>? = filteredList!![position].hashtag

        var hashtagSize = 0
        if (hashtagList != null) {
            for (item in hashtagList){
                holder.hashtag[hashtagSize].visibility = View.VISIBLE
                holder.hashtag[hashtagSize].text = "#" + item
                hashtagSize++
            }
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById(R.id.rv_item_feed_title) as TextView
        var src = itemView.findViewById(R.id.rv_item_feed_src) as ImageView
        var thumbnail = itemView.findViewById(R.id.rv_item_feed_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_feed_container) as RelativeLayout
        var hashtagContainer = itemView.findViewById(R.id.rv_item_hashtag_container) as LinearLayout
        var hashtag1 = itemView.findViewById(R.id.rv_item_feed_hashtag1) as TextView
        var hashtag2 = itemView.findViewById(R.id.rv_item_feed_hashtag2) as TextView
        var hashtag3 = itemView.findViewById(R.id.rv_item_feed_hashtag3) as TextView
        var hashtag4 = itemView.findViewById(R.id.rv_item_feed_hashtag4) as TextView
        var hashtag5 = itemView.findViewById(R.id.rv_item_feed_hashtag5) as TextView
        var hashtag = arrayListOf(hashtag1, hashtag2, hashtag3, hashtag4, hashtag5)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()){
                    filteredList = list
                }
                else{
                    val filteringList = ArrayList<Feed>()
                    for(item in list) {
                        if (item.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(item)
                        }
                    }
                    filteredList = filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as ArrayList<Feed>
                notifyDataSetChanged()
            }
        }
    }
}