package com.example.bgg_1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.BoardGameActivity
import com.example.bgg_1.R
import com.example.bgg_1.dtos.GameDTO
import com.squareup.picasso.Picasso
import com.example.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.boardgame_list_item.view.*

class BoardGameAdapter(private val model: BoardGamesViewModel) :
    RecyclerView.Adapter<BoardGamesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.boardgame_list_item, parent, false)
        return BoardGamesViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    override fun onBindViewHolder(holder: BoardGamesViewHolder, position: Int) {
        model.games.value?.get(position)?.let { holder.bindTo(it) }
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
            var rating = game.average_user_rating
            if (rating == null)
                rating = 0.0
            itemView.txvRating.text = "%.1f".format(rating)
            Picasso.get().load(game.image_url).into(itemView.imgThumbnail)
        }
        this.currentBoardGame = game
    }
}