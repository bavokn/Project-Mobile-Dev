package com.isel.bgg_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.*
import com.isel.bgg_1.GameListActivity
import kotlinx.android.synthetic.main.custom_list_game_item.view.txvButton
import kotlinx.android.synthetic.main.custom_list_item.view.*
import java.io.File

class CustomListsAdapter(
    private val context: Context,
    private val customLists: ArrayList<String>,
    val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<CustomListsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, customLists[position], position, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_list_item, parent, false)
        return ViewHolder(v, context)
    }

    override fun getItemCount(): Int {
        return customLists.size
    }

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var currentGameList: String? = null

        init {
            itemView.setOnClickListener {
                Log.e("Click test", currentGameList.toString())
                currentGameList?.let {
                    Log.d("Name: ", currentGameList!!)

                    val intent = Intent(context, GameListActivity::class.java)

                    intent.putExtra("listname", currentGameList!!)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(
            context: Context,
            customList: String,
            pos: Int,
            clickListener: (Int) -> Unit
        ) {
            itemView.txvListTitle.text = customList.substringBeforeLast(".")
            itemView.txvButton.setOnClickListener { clickListener(adapterPosition) }
            this.currentGameList = customList
        }
    }

    private fun deleteListFile(fileName: String, context: Context) {
        val path = File(context.getExternalFilesDir(null), "/customGameLists/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to delete the file.")

            File(path, "$fileName.json").delete()
        }
    }
}