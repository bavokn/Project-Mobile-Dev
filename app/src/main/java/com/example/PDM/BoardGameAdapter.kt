package com.example.PDM

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.PDM.dtos.GameDTO
import isel.leic.i1920.pdm.li51n.viewmodel.BoardGamesViewModel

class BoardGameAdapter(private val model : BoardGamesViewModel) : RecyclerView.Adapter<BoardGamesViewHolder>()
{

//TODO create this class

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
                //TODO set the correct view of the window
            .inflate(R.layout.album_view, parent, false) as LinearLayout
        return BoardGamesViewHolder(view)
    }

    override fun getItemCount(): Int = model.games.size

    override fun onBindViewHolder(holder: BoardGamesViewHolder, position: Int) {
        holder.bindTo(model.games[position])
    }
}

//TODO adjust this class
class BoardGamesViewHolder(private val view: LinearLayout) : RecyclerView.ViewHolder(view) {
    //TODO set the correct view attributes
//    private val txtAlbumName: TextView = view.findViewById<TextView>(R.id.txtAlbumName)
//    private val txtPlaycount: TextView = view.findViewById<TextView>(R.id.txtPlaycount)

    fun bindTo(album: GameDTO) {
        //TODO set binding
    }
}