package com.example.bgg_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.adapters.BoardGameAdapter
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import com.example.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment


const val TAG: String = "PDM Project"
const val LIKED_GAMES_FILENAME = "liked_games.json"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model)
    }
    private val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }

    private var pageCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pageCounter = 0

        // If recyclerview items are changed => Scroll back to top
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.scrollToPosition(0)
            }
        })

        // Setup recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Search button
        findViewById<Button>(R.id.buttonSearch).setOnClickListener(this)

        // Previous button (pagination)
        val previousButton = findViewById<Button>(R.id.previousButton)
        previousButton.setOnClickListener {
            onPreviousClick()
        }

        // Next button (pagination)
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            onNextClick()
        }

        // Get info about app button
        val infoButton = findViewById<Button>(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            this.startActivity(intent)
        }

        // Display top 30 games
        findViewById<Button>(R.id.top10Button).setOnClickListener {
            topTen()
        }

        // View liked games
        findViewById<Button>(R.id.likedGamesButton).setOnClickListener {
            likedGames()
        }

        // Create local file for storing liked games
        createLocalFile()
    }

    private fun likedGames() {
        val intent = Intent(this, LikedGamesActivity::class.java)
        this.startActivity(intent)
    }

    private fun createLocalFile() {
        val path = File(this.getExternalFilesDir(null),"/likedGames/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            if (File(path, LIKED_GAMES_FILENAME).readText() == "") {
                File(path, LIKED_GAMES_FILENAME).writeText("")
            }
        }
    }

    override fun onClick(v: View) {
        pageCounter = 0
        search("name")
        nextButton.isEnabled = true
        previousButton.isEnabled = false
    }

    private fun onNextClick() {
        pageCounter++
        previousButton.isEnabled = true
        search("name")
    }

    private fun onPreviousClick() {
        pageCounter--
        if (pageCounter == 0) {
            previousButton.isEnabled = false
        }
        search("name")
    }

    private fun topTen() {
        pageCounter = 0
        previousButton.isEnabled = false
        nextButton.isEnabled = false
        search("popular")
    }

    // Fetch games function
    private fun search(type: String) {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val name = txtSearchBoardGameName.text.toString()
        model.searchGames(name, pageCounter, type)

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()

                txtTotalBoardGames.text = games.size.toString()
            })
    }
}
