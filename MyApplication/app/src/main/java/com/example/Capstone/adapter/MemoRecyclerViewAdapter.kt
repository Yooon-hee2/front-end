package com.example.Capstone.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R

class MemoRecyclerViewAdapter(val ctx: Context, var memoList: ArrayList<String>) : RecyclerView.Adapter<MemoRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_memo, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.memo_text.text = memoList[position]

        holder.memo_text.setOnLongClickListener{
            showDeleteDialog(position)
            true
        }
    }

    private fun removeItem(position: Int) {
        memoList?.removeAt(position)
        notifyDataSetChanged()
    }

    fun returnCurrMemo(): ArrayList<String>? {
        return memoList
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var memo_text = itemView.findViewById(R.id.rv_item_memo) as TextView
        //var container = itemView.findViewById(R.id.rv_item_memo_container) as RelativeLayout
    }
    private fun showDeleteDialog(index : Int) {
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_delete)

        val delete = dialog.findViewById(R.id.btn_delete_memo) as TextView
        delete.setOnClickListener {
            dialog.dismiss()
            removeItem(index)
        }
        dialog.show()
    }
}