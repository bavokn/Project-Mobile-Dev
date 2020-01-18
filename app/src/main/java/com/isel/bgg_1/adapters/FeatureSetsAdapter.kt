package com.isel.bgg_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.GameListActivity
import com.isel.bgg_1.R
import com.isel.bgg_1.TAG
import com.isel.bgg_1.extensions.inflate
import kotlinx.android.synthetic.main.custom_list_game_item.view.*
import kotlinx.android.synthetic.main.custom_list_game_item.view.txvButton
import kotlinx.android.synthetic.main.custom_list_item.view.*
import java.io.File

private lateinit var featureSets: ArrayList<String>

class FeatureSetsAdapter(
    private val sets: ArrayList<String>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<FeatureSetsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val featureSet = sets[position]
        holder.bind(featureSet, clickListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = parent.inflate(R.layout.custom_list_item, false)
        featureSets = sets
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = sets.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
        private var set: String? = ""

        fun bind(
            set: String,
            clickListener: (Int) -> Unit
        ) {
            view.setOnClickListener { clickListener(adapterPosition) }

            this.set = set
            view.txvListTitle.text = set
        }
    }
}