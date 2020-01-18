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
import kotlinx.android.synthetic.main.custom_list_game_item.view.*
import org.json.JSONObject

class CustomListGamesAdapter(
    private val games: ArrayList<GameDTO>,
    private val context: Context,
    private val json: JSONObject,
    val clickListener: (Int) -> Unit
)
    : RecyclerView.Adapter<CustomListGamesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, games[position],json, position, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_list_game_item, parent, false)
        return ViewHolder(v, context)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    class ViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
        var currentBoardGame: GameDTO? = null

        init {
            itemView.setOnClickListener {
                Log.e("Click test", currentBoardGame!!.name.toString())
                currentBoardGame?.let {
                    Log.d("Name: ", currentBoardGame!!.name.toString())

                    val intent = Intent(context, BoardGameActivity::class.java)

                    intent.putExtra("boardgame", currentBoardGame)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(
            context: Context,
            game: GameDTO,
            json: JSONObject,
            pos: Int,
            clickListener: (Int) -> Unit
        ) {
            itemView.txvTitle.text = game.name
            itemView.txvRating.text = String.format("%.1f",game.average_user_rating)
            itemView.txvButton.setOnClickListener { clickListener(adapterPosition) }
            this.currentBoardGame = game

        }

    }
}