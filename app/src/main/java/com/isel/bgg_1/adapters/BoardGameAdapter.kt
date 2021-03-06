package com.isel.bgg_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.BoardGameActivity
import com.isel.bgg_1.R
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.boardgame_list_item.view.*

class BoardGameAdapter(private val model: BoardGamesViewModel, private val mode: String) :
    RecyclerView.Adapter<BoardGamesViewHolder>() {
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.boardgame_list_item, parent, false)
        return BoardGamesViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun getCurrentPosition(): Int = currentPosition

    override fun onBindViewHolder(holder: BoardGamesViewHolder, position: Int) {
        currentPosition = position
        model.games.value!![position].let { holder.bindTo(it) }
    }
}

class BoardGamesViewHolder(private val view: View, val context: Context) :
    RecyclerView.ViewHolder(view) {
    var currentBoardGame: GameDTO? = null

    init {
        itemView.setOnClickListener {
            currentBoardGame?.let {
                Log.d("name test", currentBoardGame!!.name!!)

                val intent = Intent(context, BoardGameActivity::class.java)

                intent.putExtra("boardgame", currentBoardGame!!)
                context.startActivity(intent)
            }
        }
    }

    fun bindTo(game: GameDTO) {
        game.let {
            itemView.txvTitle.text = game.name
            val rating = game.average_user_rating
            itemView.txvRating.text = String.format("%.1f", rating)
            Picasso.get().load(game.image_url).into(itemView.imgThumbnail)
        }
        this.currentBoardGame = game
    }
}