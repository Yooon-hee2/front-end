package com.example.Capstone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R
import com.example.Capstone.network.data.FolderData

class FolderRecyclerViewAdapter(val ctx: Context, var folderList: ArrayList<FolderData>) : RecyclerView.Adapter<FolderRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_folder, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = folderList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.folderName.text = folderList[position].folder_name
        if( folderList[position].folder_key == 0){
            holder.folderName.text = "전체"
            holder.deleteButton.visibility = View.INVISIBLE
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderName = itemView.findViewById(R.id.rv_item_folder_name) as TextView
        var deleteButton = itemView.findViewById(R.id.btn_delete_folder) as ImageView
    }
}