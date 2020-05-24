package com.example.Capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R

class HashtagRecyclerViewAdapter(val ctx: Context, var list: ArrayList<String>?, var editable: Boolean)  :

    RecyclerView.Adapter<HashtagRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_hashtag, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = list!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tagName.text = list?.get(position)

        if(editable){
            holder.tagName.setOnLongClickListener{
                removeItem(position)
                true
            }
        }
    }

    private fun removeItem(position: Int) {
        list?.removeAt(position)
        notifyDataSetChanged()
    }

    fun returnCurrHashTag(): ArrayList<String>? {
        return list
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tagName = itemView.findViewById(R.id.rv_item_hashtag) as TextView
    }


}

