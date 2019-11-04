package com.example.newapp

import BoardGame
import BoardGames
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.Math.round

class BoardGameAdapter(val context: Context, val boardGames: BoardGames) : RecyclerView.Adapter<BoardGameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boardGame = boardGames.games!![position]
        holder.setData(boardGame, position)
    }

    override fun getItemCount(): Int {
        return boardGames.games!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentBoardGame: BoardGame? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                currentBoardGame?.let {
                    Log.e("test", currentBoardGame!!.name!!)
                    val intent = Intent(context, BoardGameActivity::class.java)
                    intent.putExtra("Board Game", currentBoardGame)
                    context.startActivity(intent)
                }
            }
        }

        fun setData(boardGame: BoardGame?, pos: Int) {
            boardGame?.let {
                itemView.txvTitle.text = boardGame.name
                itemView.txvRating.text = "%.1f".format(boardGame.rating)
                Picasso.get().load(boardGame.image_url).into(itemView.imgThumbnail)
            }
            this.currentBoardGame = boardGame
            this.currentPosition = pos
        }
    }
}