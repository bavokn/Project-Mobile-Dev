package com.isel.bgg_1.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.R
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.extensions.inflate
import com.isel.bgg_1.viewmodel.FeatureSetPublishersViewModel
import kotlinx.android.synthetic.main.fs_parameter_item.view.*

private lateinit var chosenPublishers: ArrayList<String?>

class FSPublishersAdapter(private val model: FeatureSetPublishersViewModel,
                          private val clickListener: (Int, RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<FSPublishersAdapter.PublisherHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PublisherHolder {
        val inflatedView = parent.inflate(R.layout.fs_parameter_item, false)
        chosenPublishers = ArrayList()
        return PublisherHolder(inflatedView)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun addPublisher(publisher: String?) {
        chosenPublishers.add(publisher)
    }

    fun removePublisher(publisher: String?) {
        chosenPublishers.remove(publisher)
    }

    override fun onBindViewHolder(holder: PublisherHolder, position: Int) {
        val publisher = model.games.value?.get(position)!!.publishers[0]
        holder.bindItem(publisher, clickListener)
    }

    class PublisherHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var publisher: String? = null

        fun bindItem(publisher: String?,
                     clickListener: (Int, RecyclerView.ViewHolder) -> Unit) {
            view.setOnClickListener { clickListener(adapterPosition, this) }
            this.publisher = publisher

            if (chosenPublishers.contains(publisher)) {
                view.setBackgroundColor(Color.BLUE)
            } else {
                view.setBackgroundColor(Color.WHITE)
            }
            view.txvParameterTitle.text = publisher
        }
    }
}