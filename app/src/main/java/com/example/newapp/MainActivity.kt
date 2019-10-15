package com.example.newapp

import BoardGame
import BoardGameGeek
import kotlinx.coroutines.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.util.LogPrinter
import java.io.Console
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    companion object{
        private val boardGameGeek = BoardGameGeek()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch{
            search()
        }
        Log.d("first thread", "started")
    }

    fun search()
    {
        Log.d("search","succes")
        val games = boardGameGeek.searchGames("jump")
        Log.d("succes", games.toString())
    }

}
