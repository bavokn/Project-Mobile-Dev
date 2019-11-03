package com.example.newapp

import BoardGame
import BoardGameGeek
import kotlinx.coroutines.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    companion object{
        private val boardGameGeek = BoardGameGeek()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button  = findViewById(R.id.buttonSearch) as Button
        val text = findViewById(R.id.txtSearchBoardGameName) as EditText
        val view : TextView =  findViewById(R.id.txtLabelTotalBoardGames)
        button.setOnClickListener{
            Log.d("sending request", "------------")
            boardGameGeek.fetchJsonResponse(text.text.toString())
        }
    }
}
