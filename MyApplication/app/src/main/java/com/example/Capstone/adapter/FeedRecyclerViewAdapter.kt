package com.example.Capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Capstone.R
import com.example.Capstone.activities.InformationActivity
import com.example.Capstone.model.Feed
import org.jetbrains.anko.startActivity

class FeedRecyclerViewAdapter(val ctx: Context, var list: ArrayList<Feed>) :

    RecyclerView.Adapter<FeedRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_feed, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position].title

        if (list[position].src == "instagram"){
            holder.src.setImageResource(R.drawable.ic_instagram)
        }
        else{
            holder.src.setImageResource(R.drawable.ic_facebook)
        }


        if (URLUtil.isValidUrl(list[position].thumbnail)) {
            Glide.with(ctx).load(list[position].thumbnail).into(holder.thumbnail)
        }


        holder.container.setOnClickListener {
            ctx.startActivity<InformationActivity>()
        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById(R.id.rv_item_feed_title) as TextView
        var src = itemView.findViewById(R.id.rv_item_feed_src) as ImageView
        var thumbnail = itemView.findViewById(R.id.rv_item_feed_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_feed_container) as RelativeLayout
    }
}