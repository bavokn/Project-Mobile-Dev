package com.example.bgg_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.adapters.BoardGameAdapter
import com.example.bgg_1.dtos.GameDTO
import com.example.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*

const val TAG: String = "PDM Project"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model)
    }
    val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.buttonSearch).setOnClickListener(this)

        val infoButton = findViewById<Button>(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            this.startActivity(intent)
        }

        findViewById<Button>(R.id.top10Button).setOnClickListener {
            topTen()
        }
    }

    override fun onClick(v: View) {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val name = txtSearchBoardGameName.text.toString()
        model.searchGames(name, 1, "name")

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()

                txtTotalBoardGames.text = games.size.toString()
            })
    }

    fun topTen() {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        model.searchGames("", 1, "popular")

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()

                txtTotalBoardGames.text = games.size.toString()
            })
    }
}
