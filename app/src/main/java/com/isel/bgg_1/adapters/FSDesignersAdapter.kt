package com.isel.bgg_1.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.R
import com.isel.bgg_1.extensions.inflate
import com.isel.bgg_1.viewmodel.FeatureSetDesignersViewModel
import kotlinx.android.synthetic.main.fs_parameter_item.view.*

private lateinit var chosenDesigners: ArrayList<String?>

class FSDesignersAdapter(private val model: FeatureSetDesignersViewModel,
                          private val clickListener: (Int, RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<FSDesignersAdapter.DesignerHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesignerHolder {
        val inflatedView = parent.inflate(R.layout.fs_parameter_item, false)
        chosenDesigners = ArrayList()
        return DesignerHolder(inflatedView)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun addDesigner(designer: String?) {
        chosenDesigners.add(designer)
    }

    fun removeDesigner(designer: String?) {
        chosenDesigners.remove(designer)
    }

    override fun onBindViewHolder(holder: DesignerHolder, position: Int) {
        if (model.games.value?.get(position)!!.designers.size > 0) {
            val designer = model.games.value?.get(position)!!.designers[0]
            holder.bindItem(designer, clickListener)
        }
    }

    class DesignerHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var designer: String? = null

        fun bindItem(designer: String?,
                     clickListener: (Int, RecyclerView.ViewHolder) -> Unit) {
            view.setOnClickListener { clickListener(adapterPosition, this) }
            this.designer = designer

            if (chosenDesigners.contains(designer)) {
                view.setBackgroundColor(Color.BLUE)
            } else {
                view.setBackgroundColor(Color.WHITE)
            }
            view.txvParameterTitle.text = designer
        }
    }
}