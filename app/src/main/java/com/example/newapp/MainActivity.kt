package com.example.newapp

import BoardGame
import BoardGameGeek
import BoardGames
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        private val boardGameGeek = BoardGameGeek()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view : TextView =  findViewById(R.id.txtLabelTotalBoardGames)
        val totalView : TextView = findViewById(R.id.txtTotalBoardGames)

        boardGameGeek.fetchJsonResponse("game")

//
//
//        val boardGameList = listOf(bg1, bg2, bg3, bg4, bg5)
//        val boardGames = BoardGames(boardGameList)
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        recyclerView.layoutManager = layoutManager
//
//        val adapter = BoardGameAdapter(this, boardGames)
//        recyclerView.adapter = adapter
//
//        totalView.text = boardGames.games?.size.toString()
    }
}
