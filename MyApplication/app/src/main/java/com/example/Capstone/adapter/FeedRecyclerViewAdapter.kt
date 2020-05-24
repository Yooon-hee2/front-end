package com.example.Capstone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Capstone.R
import com.example.Capstone.activities.InformationActivity
import com.example.Capstone.model.Feed


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

        if (filteredList!![position].domain == "instagram"){
            holder.src.setImageResource(R.drawable.ic_instagram)
        }
        else if (filteredList!![position].domain == "facebook"){
            holder.src.setImageResource(R.drawable.ic_facebook)
        }
        else if (filteredList!![position].domain == "youtube" || filteredList!![position].domain == "youtu" ){
            holder.src.setImageResource(R.drawable.ic_youtube)
        }
        else if (filteredList!![position].domain == "naver"){
            holder.src.setImageResource(R.drawable.ic_naver)
        }
        else{
            holder.src.setImageResource(R.drawable.ic_internet)
        }

        if (URLUtil.isValidUrl(filteredList!![position].thumbnail)) {
            Glide.with(ctx)
                .load(filteredList!![position].thumbnail)
                .into(holder.thumbnail)
        }

        holder.container.setOnClickListener {
            val intent = Intent(ctx, InformationActivity::class.java)
            intent.putExtra("id", filteredList!![position].id)
            ctx.startActivity(intent)
        }

        var hashtagList: ArrayList<String> = ArrayList()

        if (filteredList!![position].tags != null) {
            for(hashTag in filteredList!![position].tags){
                hashtagList.add(hashTag.tag_text)
            }
        }

        var hashTagRecyclerViewAdapter = HashtagRecyclerViewAdapter(ctx, hashtagList, false)
        holder.hashtagContainer.adapter = hashTagRecyclerViewAdapter
        holder.hashtagContainer.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById(R.id.rv_item_feed_title) as TextView
        var src = itemView.findViewById(R.id.rv_item_feed_src) as ImageView
        var thumbnail = itemView.findViewById(R.id.rv_item_feed_thumbnail) as ImageView
        var container = itemView.findViewById(R.id.rv_item_feed_container) as RelativeLayout
        var hashtagContainer = itemView.findViewById(R.id.rv_item_hashtag_container_main) as RecyclerView
    }

    fun calcDiff(newList: ArrayList<Feed>) {
        val tileDiffUtilCallback = DataDiffUtilCallback(filteredList!!, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(tileDiffUtilCallback)
        filteredList!!.clear()
        filteredList!!.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DataDiffUtilCallback(
        private var oldData: ArrayList<Feed>,
        private var newData: ArrayList<Feed>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldData.size
        override fun getNewListSize(): Int = newData.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition)
        }
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

