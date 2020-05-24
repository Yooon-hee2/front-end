package com.example.Capstone.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Capstone.R
import com.example.Capstone.db.SharedPreferenceController
import com.example.Capstone.network.ApplicationController
import com.example.Capstone.network.NetworkService
import com.example.Capstone.network.data.FolderData
import com.example.Capstone.network.delete.DeleteFolderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolderRecyclerViewAdapter(val ctx: Context, var folderList: ArrayList<FolderData>) : RecyclerView.Adapter<FolderRecyclerViewAdapter.Holder>() {


    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_folder, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = folderList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.folderName.text = folderList[position].folder_name
        if(folderList[position].folder_key == 0){
            holder.folderName.text = "전체"
            holder.deleteButton.visibility = View.INVISIBLE
        }

        holder.deleteButton.setOnClickListener {
            showDeleteDialog(position)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderName = itemView.findViewById(R.id.rv_item_folder_name) as TextView
        var deleteButton = itemView.findViewById(R.id.btn_delete_folder) as ImageView
    }

    private fun showDeleteDialog(index : Int) {
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_delete)

        val delete = dialog.findViewById(R.id.btn_delete_memo) as TextView
        delete.setOnClickListener {
            dialog.dismiss()
            folderDeleteResponseData(SharedPreferenceController.getUserId(ctx)!!, folderList[index].folder_id)
            folderList.removeAt(index)
            notifyItemRemoved(index)
        }
        dialog.show()
    }

    private fun folderDeleteResponseData(userId : Int, folderId : Int) {

        val deleteFolderResponse: Call<DeleteFolderResponse> =
            networkService.deleteFolderResponse("application/json", userId, folderId)

        deleteFolderResponse.enqueue(object : Callback<DeleteFolderResponse> {
            override fun onFailure(call: Call<DeleteFolderResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<DeleteFolderResponse>, response: Response<DeleteFolderResponse>) {
                if (response.isSuccessful) {
                    folderList.clear()
                    if(response.body() != null){
                        for(folder in response.body()!!.folders){
                            folderList.add(folder)
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        })
    }
}