package com.example.PDM.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.PDM.R
import com.example.PDM.dtos.GameDTO
import isel.leic.i1920.pdm.li51n.viewmodel.BoardGamesViewModel

class BoardGameAdapter(private val model : BoardGamesViewModel) : RecyclerView.Adapter<BoardGamesViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
                //TODO set the correct view of the window
            .inflate(R.layout.activity_board_game, parent, false) as LinearLayout
        return BoardGamesViewHolder(view)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    override fun onBindViewHolder(holder: BoardGamesViewHolder, position: Int) {
        model.games.value?.get(position)?.let { holder.bindTo(it) }
    }
}

//TODO adjust this class
class BoardGamesViewHolder(private val view: LinearLayout) : RecyclerView.ViewHolder(view) {
    //TODO set the correct view attributes
//    private val txtAlbumName: TextView = view.findViewById<TextView>(R.id.txtAlbumName)
//    private val txtPlaycount: TextView = view.findViewById<TextView>(R.id.txtPlaycount)

    fun bindTo(game: GameDTO) {
        //TODO set binding
    }
}