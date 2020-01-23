package com.isel.bgg_1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.BoardGameAdapter
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_publisher_games.*

class PublisherActivity : AppCompatActivity() {

    val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model, "")
    }
    val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publisher_games)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_publisher_games)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setUp()
    }

    private fun setUp() {

        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val publisher = intent.getStringExtra("publisher")

        model.searchGames(publisher!!, 1, "publisher")

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()
            })

        tv_publisher_name.text = publisher
    }
}