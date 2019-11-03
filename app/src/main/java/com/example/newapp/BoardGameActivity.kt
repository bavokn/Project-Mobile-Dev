package com.example.newapp

import BoardGame
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.util.Observer

class BoardGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_game)


        setUp()
    }

    private fun setUp() {
        val boardGame = intent.getParcelableExtra<BoardGame>("Board Game")

        val nameView : TextView = findViewById(R.id.tv_bg_name)
        val yearView : TextView = findViewById(R.id.tv_bg_year)

        nameView.text = boardGame!!.name
        yearView.text = boardGame.yearpublished.toString()

        val backBtn : Button = findViewById(R.id.backButton)

        backBtn.setOnClickListener {
            finish()
        }
    }
}
