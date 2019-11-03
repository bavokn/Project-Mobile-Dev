package com.example.newapp

import BoardGame
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.ScrollingMovementMethod
import android.widget.LinearLayout

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
        val minAgeView : TextView = findViewById(R.id.tv_bg_min_age)
        val minPlayersView : TextView = findViewById(R.id.tv_bg_min_players)
        val maxPlayersView : TextView = findViewById(R.id.tv_bg_max_players)
        val ratingView : TextView = findViewById(R.id.tv_bg_rating)
        val urlView : TextView = findViewById(R.id.tv_bg_url)
        val imgUrlView : ImageView = findViewById(R.id.iv_bg_img_url)
        val creatorsView : RecyclerView = findViewById(R.id.rv_bg_creators)
        val publisherView : Button = findViewById(R.id.btn_bg_publisher)
        val descriptionView : TextView = findViewById(R.id.tv_bg_description)

        nameView.text = boardGame!!.name
        yearView.text = boardGame.year_published.toString()

        val minAgeString = "Minimum age: " + boardGame.min_age.toString()
        minAgeView.text = minAgeString

        val minPlayersString = "Minimum players: " + boardGame.min_players.toString()
        minPlayersView.text = minPlayersString

        val maxPlayersString = "Maximum players: " + boardGame.max_players.toString()
        maxPlayersView.text = maxPlayersString

        val ratingString = "Rating: " + boardGame.rating.toString()
        ratingView.text = ratingString

        urlView.text = boardGame.url
        urlView.setTextColor(Color.BLUE)

        Picasso.get().load(boardGame.image_url).into(imgUrlView)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        creatorsView.layoutManager = layoutManager

        val adapter = CreatorAdapter(this, boardGame.creators)
        creatorsView.adapter = adapter

        publisherView.text = boardGame.company
        publisherView.setOnClickListener {
            val intent = Intent(this, PublisherActivity::class.java)
            intent.putExtra("Publisher", boardGame.company)
            this.startActivity(intent)
        }

        descriptionView.text = boardGame.desc
        descriptionView.movementMethod = ScrollingMovementMethod()
    }
}
