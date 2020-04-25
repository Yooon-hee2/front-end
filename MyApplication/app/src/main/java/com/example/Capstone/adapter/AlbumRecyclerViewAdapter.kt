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
import org.jetbrains.anko.startActivity

class AlbumRecyclerViewAdapter(val ctx: Context, var list: ArrayList<String>) : RecyclerView.Adapter<AlbumRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_album, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (URLUtil.isValidUrl(list[position])) {
            Glide.with(ctx).load(list[position]).into(holder.thumbnail)
        }
        holder.container.setOnClickListener {
            ctx.startActivity<InformationActivity>()
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail = itemView.findViewById(R.id.rv_item_album_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_album_container) as RelativeLayout
    }
}