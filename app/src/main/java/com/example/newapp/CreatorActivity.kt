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

        val bg1 = BoardGame("654152", "test1.com", "test1", 1997,
            4, 8, 3, "test game about testing test test test",
            "good Commpany", arrayOf("James", "Will"), 8.5, "test1url.com")
        val bg2 = BoardGame("654153", "https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459__340.png", "test2", 2000,
            3, 6, 16, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin congue augue diam, sit amet auctor nunc ornare et. Proin porta tincidunt nisi, vitae fermentum metus finibus sit amet. Nam consectetur quis nisl ac efficitur. Nullam ac augue quis magna tincidunt placerat. Integer a convallis nisi. Suspendisse quis felis enim. Praesent sed ipsum quis erat volutpat vestibulum. Maecenas nulla diam, vehicula et ipsum nec, rutrum laoreet arcu. Maecenas facilisis massa at dignissim congue. Sed magna libero, ultrices eu leo vitae, tempus fringilla purus.\n" +
                    "\n" +
                    "Suspendisse id purus et magna sollicitudin fermentum. Suspendisse potenti. Mauris non enim sed nunc finibus ullamcorper ac id libero. Mauris id dolor lectus. Cras convallis semper lectus. Aenean ex ipsum, dictum in lectus vitae, tempus aliquam quam. Pellentesque a lacinia metus. Sed facilisis eleifend risus, quis ullamcorper diam volutpat ut. Proin fringilla, erat quis facilisis faucibus, ex metus gravida libero, non egestas risus ligula vitae mauris. Aliquam pellentesque suscipit viverra. Suspendisse potenti. Duis id suscipit sapien.\n" +
                    "\n" +
                    "Etiam eu sodales quam. Nam rutrum ornare rutrum. Nunc non laoreet risus, eget cursus lorem. Maecenas id ex vitae lectus interdum ullamcorper. Cras hendrerit ultricies mauris, et dignissim quam tempus quis. Nam tempus et turpis non iaculis. Duis elementum, nulla at posuere semper, neque magna tristique quam, at euismod metus ex nec elit. Phasellus ut laoreet neque. Nulla hendrerit, leo sit amet lacinia ultricies, dui neque tincidunt diam, ac accumsan justo augue sed ligula. Nunc sed tempus nisi. Cras ipsum justo, porta pulvinar metus nec, facilisis facilisis lorem. Etiam pharetra risus quis malesuada commodo. Duis euismod sodales mattis. Duis dictum imperdiet tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" +
                    "\n" +
                    "Donec quis vestibulum erat. Praesent a quam dignissim, blandit mauris id, pellentesque erat. Proin eget tincidunt ipsum. Proin cursus quam non dui laoreet, vitae vehicula erat imperdiet. Vestibulum aliquet ligula quis lacinia scelerisque. Fusce id dui viverra, suscipit felis ut, viverra orci. Donec ut dui eu eros egestas suscipit.",
            "better company", arrayOf("William", "Smith", "West"), 7.2, "test2url.com")
        val bg3 = BoardGame("654154", "test3.com", "test3", 2003,
            3, 12, 7, "test game about testing test test test",
            "worse company", arrayOf("Jackass", "Will"), 6.1, "test3url.com")
        val bg4 = BoardGame("654155", "test4.com", "test4", 2017,
            4, 9, 12, "test game about testing test test test",
            "better company", arrayOf("Who", "What", "Why"), 9.9, "test4url.com")
        val bg5 = BoardGame("654156", "test5.com", "test5", 2005,
            2, 4, 7, "test game about testing test test test",
            "worst company", arrayOf("No one", "Will"), 2.1, "test5url.com")

        val boardGameList = listOf(bg1, bg2, bg3, bg4, bg5)
        val selectedGames = mutableListOf<BoardGame>()
        boardGameList.forEach {
            if (it.creators.contains(creator)) {
                selectedGames += it
            }
        }
        val boardGames = BoardGames(selectedGames)

        val adapter = BoardGameAdapter(this, boardGames)
        rv_creator_games.adapter = adapter

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
