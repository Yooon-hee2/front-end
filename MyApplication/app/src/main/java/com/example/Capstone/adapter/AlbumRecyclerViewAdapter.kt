package com.example.Capstone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Capstone.R
import com.example.Capstone.activities.InformationActivity
import com.example.Capstone.model.Feed
import org.jetbrains.anko.startActivity

class AlbumRecyclerViewAdapter(val ctx: Context, var list: ArrayList<Feed>)
    : RecyclerView.Adapter<AlbumRecyclerViewAdapter.Holder>() , Filterable{

    private var filteredList: ArrayList<Feed>? = null

    init {
        this.filteredList = list
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_album, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = filteredList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (URLUtil.isValidUrl(filteredList!![position].thumbnail)) {
            Glide.with(ctx).load(filteredList!![position].thumbnail).into(holder.thumbnail)
        }

        holder.container.setOnClickListener {
            val intent = Intent(ctx, InformationActivity::class.java)
            intent.putExtra("id", filteredList!![position].id)
            intent.putExtra("noti", false)
            ctx.startActivity(intent)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail = itemView.findViewById(R.id.rv_item_album_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_album_container) as RelativeLayout
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()){
                    filteredList = list
                }
                else{
                    if(charString.substring(0,1) == "#"){ //if user search hashtag
                        val filteringList = ArrayList<Feed>()
                        for(item in list) {
                            if (item.tags!!.isNotEmpty()){
                                for (ht in item.tags!!){
                                    if (ht.tag_text.contains(charString.substring(1, charString.length).toLowerCase())) {
                                        filteringList.add(item)
                                    }
                                }
                            }
                        }
                        val hashSet = HashSet<Feed>() //remove duplication
                        filteringList.let { hashSet.addAll(it) }
                        filteringList.clear()
                        filteringList.addAll(hashSet)
                        filteredList = filteringList
                    }
                    else{
                        val filteringList = ArrayList<Feed>() //if user search title
                        for(item in list) {
                            if (item.title.toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(item)
                            }
                        }
                        filteredList = filteringList
                    }
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