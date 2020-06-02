package com.example.Capstone.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.model.Storage

class StorageRecyclerViewAdapter(val ctx: Context, var storageList: ArrayList<Storage>) : RecyclerView.Adapter<StorageRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_folder, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = storageList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.storageName.text = storageList[position].sharing_name
        holder.deleteButton.visibility = View.GONE

        holder.storageContainer.setOnClickListener{
            (ctx as Activity).finish()
            SharedPreferenceController.setCurrentUserId(ctx, storageList[position].id)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storageName = itemView.findViewById(R.id.rv_item_folder_name) as TextView
        var deleteButton = itemView.findViewById(R.id.btn_delete_folder) as ImageView
        var storageContainer = itemView.findViewById(R.id.rv_item_folder) as RelativeLayout
    }
}