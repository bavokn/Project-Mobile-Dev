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
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import com.example.bgg_1.viewmodel.BoardGamesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.boardgame_list_item.view.*
import org.json.JSONObject

class LikedGamesAdapter(private val model: BoardGamesViewModel) :
    RecyclerView.Adapter<LikedGamesViewHolder>() {
    override fun onBindViewHolder(holder: LikedGamesViewHolder, position: Int) {
        currentPosition = position
        json = JSONObject()
    }

    private var currentPosition = 0
    private var json = JSONObject()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedGamesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.liked_game_list_item, parent, false)
        return LikedGamesViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int = model.games.value?.size ?: 0

    fun getCurrentPosition(): Int = currentPosition

    fun setJson(json: JSONObject) {
        this.json = json
    }

}

class LikedGamesViewHolder(private val view: View, val context: Context) :
    RecyclerView.ViewHolder(view) {
    var currentBoardGame: JSONObject? = null

    fun bindTo(game: JSONObject) {
        game.let {
            itemView.txvTitle.text = game["name"].toString()
            val rating = game["average_user_rating"] as Double
            itemView.txvRating.text = String.format("%.1f", rating)
            Picasso.get().load(game["image_url"].toString()).into(itemView.imgThumbnail)
        }
        this.currentBoardGame = game
    }
}