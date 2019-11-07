package com.example.PDM.adapters

import BoardGame
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.PDM.BoardGameActivity
import com.example.PDM.R
import com.example.PDM.dtos.GameDTO
import com.squareup.picasso.Picasso
import isel.leic.i1920.pdm.li51n.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.list_item.view.*

class BoardGameAdapter(private val model : BoardGamesViewModel) : RecyclerView.Adapter<BoardGamesViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
                //TODO set the correct view of the window
            .inflate(R.layout.list_item, parent, false)
        return BoardGamesViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    override fun onBindViewHolder(holder: BoardGamesViewHolder, position: Int) {
        model.games.value?.get(position)?.let { holder.bindTo(it) }
    }
}

//TODO adjust this class
class BoardGamesViewHolder(private val view: View, val context: Context) : RecyclerView.ViewHolder(view) {
    var currentBoardGame: GameDTO? = null

    init {
        itemView.setOnClickListener {
            currentBoardGame?.let {
                val intent = Intent(context, BoardGameActivity::class.java)
                intent.putExtra("Board Game", currentBoardGame)
                context.startActivity(intent)
            }
        }
    }

    //TODO set the correct view attributes
//    private val txtAlbumName: TextView = view.findViewById<TextView>(R.id.txtAlbumName)
//    private val txtPlaycount: TextView = view.findViewById<TextView>(R.id.txtPlaycount)

    fun bindTo(game: GameDTO) {
        //TODO set binding
        game.let {
            itemView.txvTitle.text = game.name
            Log.d("game", game.rating.toString())
            itemView.txvRating.text = "%.1f".format(game.rating)
            Picasso.get().load(game.image_url).into(itemView.imgThumbnail)
        }
        this.currentBoardGame = game
    }
}