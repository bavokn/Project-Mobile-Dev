package com.example.bgg_1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.adapters.LikedGamesAdapter
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.File
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class LikedGamesActivity : AppCompatActivity() {

    private var pageCounter: Int = 0
    private lateinit var json: JSONObject
    private lateinit var games: ArrayList<GameDTO>
    private lateinit var adapter: LikedGamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_games)

        pageCounter = 0
        readJson()

        games = games()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // rv_parts.adapter = PartAdapter(testData, { partItem : PartData -> partItemClicked(partItem) })
        adapter = LikedGamesAdapter(games, this, json) { pos: Int -> itemDelete(pos,
            games[pos].id!!, games[pos]) }
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.scrollToPosition(0)
            }
        })

        // Maybe add pagination later
        """val previousButton = findViewById<Button>(R.id.previousButton)
        previousButton.setOnClickListener {
            onPreviousClick()
        }

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            onNextClick()
        }"""
    }

    private fun onNextClick() {
        pageCounter++
        previousButton.isEnabled = true
        games()
    }

    private fun onPreviousClick() {
        pageCounter--
        if (pageCounter == 0) {
            previousButton.isEnabled = false
        }
        games()
    }

    private fun itemDelete(pos : Int, id: String, gameDTO: GameDTO) {
        json.remove(id)
        games.remove(gameDTO)
        adapter.notifyItemRemoved(pos)
        adapter.notifyItemRangeChanged(0, games.size)
        writeToFile(json, this)
        Toast.makeText(this, "Deleted: ${gameDTO.name}", Toast.LENGTH_LONG).show()
    }

    private fun writeToFile(json: JSONObject, context: Context) {
        val path = File(this.getExternalFilesDir(null), "/likedGames/")
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

    private fun readJson() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/likedGames/")
        val jsonText = File(path, LIKED_GAMES_FILENAME).readText()
        json = JSONObject()
        if (jsonText != "") {
            json = JSONObject(jsonText)
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
                average_user_rating, url)

            gameDTOs.add(gameDTO)
        }

        for (game in gameDTOs) {
            Log.e(TAG, game.name.toString())
        }

        return gameDTOs
    }
}
