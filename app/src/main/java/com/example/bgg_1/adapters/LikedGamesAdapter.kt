package com.example.bgg_1.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.LIKED_GAMES_FILENAME
import com.example.bgg_1.R
import com.example.bgg_1.TAG
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import kotlinx.android.synthetic.main.liked_game_list_item.view.*
import org.json.JSONObject
import java.io.File

class LikedGamesAdapter(
    private val games: ArrayList<GameDTO>,
    private val context: Context,
    private val json: JSONObject,
    val clickListener: (Int) -> Unit
)
    : RecyclerView.Adapter<LikedGamesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, games[position],json, position, clickListener)

        """holder.txtName.text = games[position].name
        holder.txtRating.text = String.format("%.1f",games[position].average_user_rating)"""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.liked_game_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
            """itemView.txvButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)

                with(builder)
                {
                    setTitle("Remove Game")
                    setMessage("Are you sure you want to remove this game?")
                    setPositiveButton(
                        android.R.string.yes,
                        positiveButtonClick(game.id!!, pos, json, clickListener, context)
                    )
                    setNegativeButton(android.R.string.no, negativeButtonClick())
                    show()
                }
            }"""
        }

        private fun negativeButtonClick(): DialogInterface.OnClickListener? {
            return null
        }

        private fun positiveButtonClick(
            id: String,
            position: Int,
            json: JSONObject,
            clickListener: (Int) -> Unit,
            context: Context
        ): DialogInterface.OnClickListener? {
            json.remove(id)
            writeToFile(json, context)
            clickListener(position)
            return null
        }

        private fun writeToFile(json: JSONObject, context: Context) {
            val path = File(context.getExternalFilesDir(null), "/likedGames/")
            var success = true

            if (!path.exists()) {
                success = path.mkdir()
                Log.d(TAG, "Directory $path was created: $success")
            }

            if (success) {
                Log.d(TAG, "Directory exist, proceed to create the file.")
                val text = json.toString()

                File(path, LIKED_GAMES_FILENAME).writeText(text)
            }
        }
    }
}