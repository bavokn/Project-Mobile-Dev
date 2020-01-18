package com.isel.bgg_1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.CustomListGamesAdapter
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import kotlinx.android.synthetic.main.activity_custom_list_games.*
import org.json.JSONObject
import java.io.File

/*

This is the activity for a chosen custom list of games.
This activity thus contains a recyclerview of all the games in said list.

 */

class GameListActivity : AppCompatActivity() {

    private var pageCounter: Int = 0
    private lateinit var json: JSONObject
    private lateinit var games: ArrayList<GameDTO>
    private lateinit var adapter: CustomListGamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_list_games)

        val gameList = intent.getStringExtra("listname")!!
        listNameTv.text = gameList.substringBeforeLast(".")

        pageCounter = 0
        readJson(gameList)

        games = games()

        val recyclerView = findViewById<RecyclerView>(R.id.featureSetsRv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CustomListGamesAdapter(games, this, json) { pos: Int -> itemDelete(pos,
            games[pos].id!!, games[pos], gameList) }
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.scrollToPosition(0)
            }
        })
    }

    private fun itemDelete(pos : Int, id: String, gameDTO: GameDTO, gameList: String) {
        json.remove(id)
        games.remove(gameDTO)
        adapter.notifyItemRemoved(pos)
        adapter.notifyItemRangeChanged(0, games.size)
        writeToFile(this, json, gameList)
        Toast.makeText(this, "Deleted: ${gameDTO.name}", Toast.LENGTH_LONG).show()
    }

    private fun writeToFile(context: Context, json: JSONObject, gameList: String) {
        val path = File(this.getExternalFilesDir(null), "/customGameLists/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            val text = json.toString()

            File(path, gameList).writeText(text)
        }
    }

    private fun readJson(gameList: String) {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/customGameLists/")
        val jsonText = File(path, gameList).readText()
        json = if (jsonText != "") {
            JSONObject(jsonText)
        } else {
            JSONObject()
        }
    }
    private fun games(): ArrayList<GameDTO> {

        val gameDTOs = arrayListOf<GameDTO>()

        for (key in json.keys()) {
            val data = json[key] as JSONObject
            val image_url = data["image_url"].toString()
            val name = data["name"].toString()
            val year_published: Int? = data["year_published"] as Int
            val min_players:Int? = data["min_players"] as Int
            val max_players:Int? = data["max_players"] as Int
            val min_age:Int? = data["min_age"] as Int
            val description = data["description"].toString()
            val primary_publisher = if (data.has("primary_publisher")) {
                data["primary_publisher"].toString()
            } else {
                ""
            }
            val designers = if (data.has("designers")) {
                ArrayList(data["designers"].toString().substring(1, data["designers"]
                    .toString().length - 1).trim().splitToSequence(',')
                    .filter { it.isNotEmpty() }
                    .toList())
            } else {
                ArrayList()
            }
            val artists = if (data.has("artists")) {
                ArrayList(data["artists"].toString().substring(1, data["artists"]
                    .toString().length - 1).trim().splitToSequence(',')
                    .filter { it.isNotEmpty() }
                    .toList())
            } else {
                ArrayList()
            }
            val average_user_rating = data["average_user_rating"].toString().toDouble()
            val url = data["url"].toString()

            val gameDTO = GameDTO(key, image_url, name,
                year_published!!, min_players!!,
                max_players!!, min_age!!, description, primary_publisher, designers, artists,
                average_user_rating, url
            )

            gameDTOs.add(gameDTO)
        }

        return gameDTOs
    }
}
