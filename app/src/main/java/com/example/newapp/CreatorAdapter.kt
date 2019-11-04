package com.example.newapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class CreatorAdapter(val context: Context, val creators: Array<String?>) : RecyclerView.Adapter<CreatorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val creator = creators[position]
        holder.setData(creator, position)
    }

    override fun getItemCount(): Int {
        return creators.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentCreator: String? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentCreator?.let {
                    Log.e("test", currentCreator!!)
                    val intent = Intent(context, CreatorActivity::class.java)
                    intent.putExtra("Creator", currentCreator)
                    context.startActivity(intent)
                }
            }
        }

        fun setData(creator: String?, pos: Int) {
            creator?.let {
                itemView.txvTitle.text = creator
                itemView.txvRating.text = ""
            }
            this.currentCreator = creator
            this.currentPosition = pos
        }
    }
}