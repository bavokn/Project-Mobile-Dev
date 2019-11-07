package com.example.PDM

import BoardGameGeek
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
import com.example.PDM.adapters.BoardGameAdapter
import com.example.PDM.dtos.GameDTO
import isel.leic.i1920.pdm.li51n.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*
const val TAG : String = "PDM Project"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val adapter : BoardGameAdapter by lazy {
        BoardGameAdapter(model)
    }
    val model : BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Setup recyclerArtists with BoardGameAdapter
         */
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        /**
         * Setup search button
         */
        findViewById<Button>(R.id.buttonSearch).setOnClickListener(this)

        val infoButton = findViewById<Button>(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onClick(v: View) {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Last.fm...")
        val name = txtSearchBoardGameName.text.toString()
        model.searchGames(name, 1)

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()
                //TODO update UI
                txtTotalBoardGames.text = games.size.toString()
            })
    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val searchButton  = findViewById<Button>(R.id.buttonSearch)
//        val infoButton = findViewById<Button>(R.id.infoButton)
//        val top10Button = findViewById<Button>(R.id.top10Button)
//        val searchText = findViewById<EditText>(R.id.txtSearchBoardGameName)
//        val totalGamesView : TextView =  findViewById(R.id.txtLabelTotalBoardGames)
//        val totalView : TextView = findViewById(R.id.txtTotalBoardGames)
//
//        searchButton.setOnClickListener{
//            boardGameGeek.fetchJsonResponse(searchText.text.toString())
//
//            Log.e("CHECK: ", "BEFORE IF CHECK")
//
//            if (boardGameGeek.boardGames?.games !== null) {
//                Log.e("CHECK2: ", "AFTER IF CHECK")
//                val boardGames = boardGameGeek.boardGames
//                if (boardGames != null) {
//                    totalView.text = boardGames.games?.size.toString()
//                }
//                val layoutManager = LinearLayoutManager(this)
//                layoutManager.orientation = LinearLayoutManager.VERTICAL
//                recyclerView.layoutManager = layoutManager
//
//                val adapter = boardGames?.let { BoardGameAdapter(this, it) }
//                recyclerView.adapter = adapter
//                Log.d("sorted", topTen().toString())
//            }
//        }
//
//        infoButton.setOnClickListener {
//            val intent = Intent(this, InfoActivity::class.java)
//            this.startActivity(intent)
//        }
//    }

    fun topTen(){
        //TODO make new request here with the extra params sort_by = average_user_rating
    }
}
