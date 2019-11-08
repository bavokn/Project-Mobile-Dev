package com.example.bgg_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.CreatorActivity
import com.example.bgg_1.R
import kotlinx.android.synthetic.main.boardgame_list_item.view.txvTitle
import kotlinx.android.synthetic.main.creator_list_item.view.*

class CreatorAdapter(val context: Context, val creators: ArrayList<ArrayList<String?>>) :
    RecyclerView.Adapter<CreatorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.creator_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var creator: String? = null
        var type: String? = null

        when {
            position < creators[0].size
                    && creators[0].size != 0 -> {
                creator = creators[0][position]
                type = "artist"
            }
            position < creators[0].size + creators[1].size
                    && creators[1].size != 0 -> {
                creator = creators[1][position - creators[0].size]
                type = "designer"
            }
        }

        holder.setData(creator, position, type)
    }

    override fun getItemCount(): Int {
        return creators[0].size + creators[1].size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentCreator: String? = null
        var currentPosition: Int = 0
        var currentType: String? = null

        init {
            itemView.setOnClickListener {
                currentCreator?.let {
                    Log.d("test", currentCreator!!)
                    val intent = Intent(context, CreatorActivity::class.java)
                    intent.putExtra("creator_name", currentCreator)
                    intent.putExtra("creator_type", currentType)
                    context.startActivity(intent)
                }
            }
        }

        fun setData(creator: String?, pos: Int, type: String?) {
            creator?.let {
                itemView.txvTitle.text = creator
                val typeStr = "($type)"
                itemView.txvType.text = typeStr
            }
            this.currentCreator = creator
            this.currentPosition = pos
            this.currentType = type
        }
    }
}