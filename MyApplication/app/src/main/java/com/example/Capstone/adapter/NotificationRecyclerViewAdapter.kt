package com.example.Capstone.adapter

import android.content.Context
import android.content.Intent
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

class NotificationRecyclerViewAdapter(val ctx: Context, var notiList: ArrayList<Feed>) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_notification, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = notiList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.notiTitle.text = notiList[position].title

        var tagText = ""

        for(tag in notiList[position].tags){
            tagText += tag.tag_text
        }

        holder.notiHashtag.text = tagText

        holder.container.setOnClickListener{
            val intent = Intent(ctx, InformationActivity::class.java)
            intent.putExtra("id", notiList!![position].id)
            ctx.startActivity(intent)
        }

        if (URLUtil.isValidUrl(notiList!![position].thumbnail)) {
            Glide.with(ctx)
                .load(notiList!![position].thumbnail)
                .into(holder.notiThumbnail)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notiTitle = itemView.findViewById(R.id.rv_item_noti_title) as TextView
        var notiHashtag = itemView.findViewById(R.id.rv_item_noti_hashtag) as TextView
        var notiThumbnail = itemView.findViewById(R.id.rv_item_noti_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_noti) as RelativeLayout
    }

}