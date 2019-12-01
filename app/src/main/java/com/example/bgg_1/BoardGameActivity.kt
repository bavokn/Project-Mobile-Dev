package com.example.bgg_1

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg_1.adapters.CreatorAdapter
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_board_game.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream


class BoardGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_game)

        val boardGame = intent.getParcelableExtra<GameDTO>("boardgame")!!
        val json = getJSON(boardGame)
        val liked = checkIfLiked(json, boardGame)

        setUp(boardGame, liked, json)
    }

    private fun getJSON(boardGame: GameDTO): JSONObject {
        val path = File(filesDir,"/likedGames/")
        val jsonText = File(path, LIKED_GAMES_FILENAME).readText()
        return if (jsonText == "") {
            JSONObject()
        } else {
            JSONObject(jsonText)
        }
    }

    private fun checkIfLiked(json: JSONObject, boardGame: GameDTO): Boolean {
        val liked = json.has(boardGame.id)
        changeLikedButton(liked)
        return liked
    }

    private fun setUp(boardGame: GameDTO, liked: Boolean, json: JSONObject) {

        val nameView: TextView = findViewById(R.id.tv_bg_name)
        val yearView: TextView = findViewById(R.id.tv_bg_year)
        val minAgeView: TextView = findViewById(R.id.tv_bg_min_age)
        val minPlayersView: TextView = findViewById(R.id.tv_bg_min_players)
        val maxPlayersView: TextView = findViewById(R.id.tv_bg_max_players)
        val ratingView: TextView = findViewById(R.id.tv_bg_rating)
        val urlView: TextView = findViewById(R.id.tv_bg_url)
        val imgUrlView: ImageView = findViewById(R.id.iv_bg_img_url)
        val creatorsView: RecyclerView = findViewById(R.id.rv_bg_creators)
        val publisherView: Button = findViewById(R.id.btn_bg_publisher)
        val descriptionView: TextView = findViewById(R.id.tv_bg_description)

        nameView.text = boardGame.name
        yearView.text = boardGame.year_published.toString()

        val minAgeString = "Minimum age: " + boardGame.min_age.toString()
        minAgeView.text = minAgeString

        val minPlayersString = "Minimum players: " + boardGame.min_players.toString()
        minPlayersView.text = minPlayersString

        val maxPlayersString = "Maximum players: " + boardGame.max_players.toString()
        maxPlayersView.text = maxPlayersString

        val ratingString = "Rating: " + "%.1f".format(boardGame.average_user_rating)
        ratingView.text = ratingString

        urlView.text = boardGame.url
        urlView.setTextColor(Color.BLUE)

        Picasso.get().load(boardGame.image_url).into(imgUrlView)

        imgUrlView.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(boardGame.image_url)
            )
            startActivity(browserIntent)
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        creatorsView.layoutManager = layoutManager

        val creators: ArrayList<ArrayList<String?>> = arrayListOf(boardGame.artists)
        creators.addAll(arrayListOf(boardGame.designers))

        val adapter = CreatorAdapter(this, creators)
        creatorsView.adapter = adapter

        publisherView.text = boardGame.primary_publisher
        publisherView.setOnClickListener {
            val intent = Intent(this, PublisherActivity::class.java)
            intent.putExtra("publisher", boardGame.primary_publisher)
            this.startActivity(intent)
        }

        likeButton.setOnClickListener {
            likeButtonClick(json, boardGame)
        }

        descriptionView.text = boardGame.description
        descriptionView.movementMethod = ScrollingMovementMethod()
    }

    private fun likeButtonClick(json: JSONObject, boardGame: GameDTO) {

        val liked = checkIfLiked(json, boardGame)
        if (liked) {
            json.remove(boardGame.id)
            writeToFile(json)
            Toast.makeText(this, "Disliked", Toast.LENGTH_SHORT).show()
        } else {
            val boardGameDetails = JSONObject()
            boardGameDetails.put("image_url", boardGame.image_url)
            boardGameDetails.put("name", boardGame.name)
            boardGameDetails.put("year_published", boardGame.year_published)
            boardGameDetails.put("min_players", boardGame.min_players)
            boardGameDetails.put("max_players", boardGame.max_players)
            boardGameDetails.put("min_age", boardGame.min_age)
            boardGameDetails.put("description", boardGame.description)
            boardGameDetails.put("primary_publisher",boardGame.primary_publisher)
            boardGameDetails.put("designers", boardGame.designers)
            boardGameDetails.put("artists", boardGame.artists)
            boardGameDetails.put("average_user_rating", boardGame.average_user_rating)
            boardGameDetails.put("url", boardGame.url)

            json.put(boardGame.id!!, boardGameDetails)

            writeToFile(json)
            Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show()
        }

        checkIfLiked(json, boardGame)
    }

    private fun writeToFile(json: JSONObject) {
        val path = File(filesDir,"/likedGames/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            val text = json.toString()

            File(path, LIKED_GAMES_FILENAME).writeText(text)
        }
    }

    private fun changeLikedButton(liked: Boolean) {
        if (liked) {
            likeButton.text = String.format("DISLIKE")
            likeButton.setBackgroundColor(resources.getColor(R.color.md_red_600))
        } else {
            likeButton.text = String.format("LIKE")
            likeButton.setBackgroundColor(resources.getColor(R.color.md_blue_700))
        }
    }
}