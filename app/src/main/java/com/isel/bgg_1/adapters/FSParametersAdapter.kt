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
import kotlinx.android.synthetic.main.custom_list_game_item.view.txvTitle
import kotlinx.android.synthetic.main.fs_parameter_item.view.*

class FSParametersAdapter(
    private val context: Context,
    private val parameterValues: ArrayList<String>,
    val clickListener: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<FSParametersAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, parameterValues[position], position, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fs_parameter_item, parent, false)
        return ViewHolder(v, context)
    }

    override fun getItemCount(): Int {
        return parameterValues.size
    }

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var currentParameterValue: String? = null
        var isChecked: Boolean = false

        init {
            itemView.setOnClickListener {
                Log.e("Click test", currentParameterValue.toString())
                currentParameterValue?.let {
                    Log.d("Name: ", currentParameterValue!!)

                    val intent = Intent(context, GameListActivity::class.java)

                    intent.putExtra("listname", currentParameterValue!!)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(
            context: Context,
            parameterValue: String,
            pos: Int,
            clickListener: (Int, Boolean) -> Unit
        ) {
            itemView.txvTitle.text = parameterValue
            itemView.setOnClickListener { clickListener(adapterPosition, isChecked) }
            this.currentParameterValue = parameterValue
        }
    }
}