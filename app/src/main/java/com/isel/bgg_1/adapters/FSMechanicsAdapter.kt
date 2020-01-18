package com.isel.bgg_1.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.R
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.extensions.inflate
import com.isel.bgg_1.viewmodel.FeatureSetMechanicsViewModel
import kotlinx.android.synthetic.main.boardgame_list_item.view.*
import kotlinx.android.synthetic.main.fs_parameter_item.view.*

private lateinit var mechanics: Array<ValueDTO>
private lateinit var chosenMechanics: ArrayList<ValueDTO?>

class FSMechanicsAdapter(private val model: FeatureSetMechanicsViewModel,
                         private val clickListener: (Int, RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<FSMechanicsAdapter.MechanicHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MechanicHolder {
        val inflatedView = parent.inflate(R.layout.fs_parameter_item, false)
        mechanics = model.games.value!!
        chosenMechanics = ArrayList()
        return MechanicHolder(inflatedView)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun addMechanic(mechanic: ValueDTO?) {
        chosenMechanics.add(mechanic)
    }

    fun removeMechanic(mechanic: ValueDTO?) {
        chosenMechanics.remove(mechanic)
    }

    override fun onBindViewHolder(holder: MechanicHolder, position: Int) {
        val mechanic = model.games.value?.get(position)
        holder.bindItem(mechanic, clickListener)
    }

    class MechanicHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var mechanic: ValueDTO? = null

        fun bindItem(mechanic: ValueDTO?,
                     clickListener: (Int, RecyclerView.ViewHolder) -> Unit) {
            view.setOnClickListener { clickListener(adapterPosition, this) }
            this.mechanic = mechanic

            if (chosenMechanics.contains(mechanic)) {
                view.setBackgroundColor(Color.BLUE)
            } else {
                view.setBackgroundColor(Color.WHITE)
            }
            view.txvParameterTitle.text = mechanic?.name
        }
    }
}