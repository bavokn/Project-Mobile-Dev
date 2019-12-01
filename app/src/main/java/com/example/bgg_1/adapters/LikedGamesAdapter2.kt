package com.example.bgg_1.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.LIKED_GAMES_FILENAME
import com.example.bgg_1.R
import com.example.bgg_1.TAG
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import org.json.JSONObject
import java.io.File

class LikedGamesAdapter2(private val list: ArrayList<GameDTO>,
                        private val context: Context)
    : RecyclerView.Adapter<LikedGamesAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.liked_game_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(game: GameDTO) {
            val title: TextView = itemView.findViewById(R.id.txvTitle) as TextView
            val rating: TextView = itemView.findViewById(R.id.txvRating) as TextView

            title.text = game.name
            rating.text = String.format("%.1f" ,game.average_user_rating)

            itemView.findViewById<Button>(R.id.txvButton).setOnClickListener {
                val builder = AlertDialog.Builder(context)

                with(builder)
                {
                    setTitle("Remove Game")
                    setMessage("Are you sure you want to remove this game?")
                    setPositiveButton(android.R.string.yes, positiveButtonClick(game.id))
                    setNegativeButton(android.R.string.no, negativeButtonClick())
                    show()
                }
            }
        }

        private fun negativeButtonClick(): DialogInterface.OnClickListener? {
            return null
        }

        private fun positiveButtonClick(id: String?): DialogInterface.OnClickListener? {
            //json.remove(id)
            //writeToFile(json)
            return null
        }
    }

    private fun writeToFile(json: JSONObject) {
        val path = File(context.filesDir,"/likedGames/")
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