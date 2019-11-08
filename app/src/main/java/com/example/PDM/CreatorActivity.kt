package com.example.PDM

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.PDM.adapters.BoardGameAdapter
import com.example.PDM.dtos.GameDTO
import isel.leic.i1920.pdm.li51n.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_creator_games.*

class CreatorActivity : AppCompatActivity() {

    val adapter : BoardGameAdapter by lazy {
        BoardGameAdapter(model)
    }
    val model : BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator_games)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_creator_games)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setUp()
    }

    private fun setUp() {

        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val creator = intent.getStringExtra("creator_name")
        val type = intent.getStringExtra("creator_type")

        model.searchGames(creator!!, 1, type!!)

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()
            })

        tv_creator_name.text = creator
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}