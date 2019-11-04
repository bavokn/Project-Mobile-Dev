package com.example.newapp

import BoardGame
import BoardGames
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_creator_games.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_publisher_games.*

class CreatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator_games)

        setUp()
    }

    private fun setUp() {
        val creator = intent.getStringExtra("Creator")

        tv_creator_name.text = creator

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_creator_games.layoutManager = layoutManager

        MainActivity.boardGameGeek.boardGames?.games = null
        MainActivity.boardGameGeek.fetchJsonResponse(creator!!, "creator")

        if (MainActivity.boardGameGeek.boardGames?.games !== null) {
            val boardGames = MainActivity.boardGameGeek.boardGames

            val adapter = boardGames?.let { BoardGameAdapter(this, it) }
            rv_creator_games.adapter = adapter
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
