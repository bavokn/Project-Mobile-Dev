package com.example.bgg_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.adapters.BoardGameAdapter
import com.example.bgg_1.adapters.LikedGamesAdapter
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import com.example.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File

class LikedGamesActivity : AppCompatActivity() {

    private val adapter: LikedGamesAdapter by lazy {
        LikedGamesAdapter(model)
    }

    private val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }

    private var pageCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_games)

        pageCounter = 0

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.scrollToPosition(0)
            }
        })

        search()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
        search()
    }

    private fun onPreviousClick() {
        pageCounter--
        if (pageCounter == 0) {
            previousButton.isEnabled = false
        }
        search()
    }

    private fun search() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(filesDir,"/likedGames/")
        val jsonText = File(path, LIKED_GAMES_FILENAME).readText()
        var json = JSONObject()
        if (jsonText != "") {
            json = JSONObject(jsonText)
        }

        adapter.setJson(json)
        model.games.observe(this,
            Observer<Array<GameDTO>> { games -> adapter.notifyDataSetChanged() })
        findViewById<TextView>(R.id.testTextView).text = json.toString()

        """this.openFileInput(filesDir.absolutePath + "/"
                + LIKED_GAMES_FILENAME).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "some\ntext"
            }
        }

        val name = txtSearchBoardGameName.text.toString()
        model.searchGames(name, pageCounter, type)

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()

                txtTotalBoardGames.text = games.size.toString()
            })"""
    }
}
