package com.isel.bgg_1

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.BoardGameAdapter
import com.isel.bgg_1.boardgameatlas.BoardGameAtlasApiImpl
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.URLEncoder


const val TAG: String = "PDM Project"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model, "")
    }
    private val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }
    companion object {
        lateinit var bgg: BoardGameAtlasApiImpl
    }

    lateinit var mainHandler: Handler

    private val updateNewGames = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun run() {
            compareGames()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private var pageCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pageCounter = 0

        // If recyclerview items are changed => Scroll back to top
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                featureSetsRv.scrollToPosition(0)
            }
        })

        // Setup recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.featureSetsRv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Search button
        findViewById<Button>(R.id.buttonSearch).setOnClickListener(this)

        // Previous button (pagination)
        val previousButton = findViewById<Button>(R.id.previousButton)
        previousButton.setOnClickListener {
            onPreviousClick()
        }

        // Next button (pagination)
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            onNextClick()
        }

        // Get info about app button
        val infoButton = findViewById<Button>(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            this.startActivity(intent)
        }

        // Display top 30 games
        findViewById<Button>(R.id.top10Button).setOnClickListener {
            topTen()
        }

        // View liked games
        findViewById<Button>(R.id.customListsButton).setOnClickListener {
            customGameLists()
        }

        // View feature sets
        findViewById<Button>(R.id.featureSetsBtn).setOnClickListener {
            featureSets()
        }

        // Create local file for storing liked games
        createLocalDirectory()

        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateNewGames)
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    fun compareGames() {
        Log.e("Background loop test", "CHECK")
        var counter = 0
        File(this.getExternalFilesDir(null),"/featureSets/games/").walk().forEach {
            if (counter != 0) {
                val featureSet = it.toString().substringAfterLast("/").substringBeforeLast(".")
                val gamesJSONArray = readJson(featureSet)

                val gamesOld = ArrayList<String>()
                for (i in 0 until gamesJSONArray.length()) {
                    gamesOld.add(gamesJSONArray.getString(i))
                }

                val gamesNew = searchFeatureSetGames(featureSet)

                if (gamesNew.size > gamesOld.size) {
                    Log.e("Notification test", "GAME ADDED")
                    val name = "New Games"
                    val descriptionText = "There are new games in your $featureSet feature set."
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val mChannel = NotificationChannel("new_game_feature_set", name, importance)
                    mChannel.description = descriptionText
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(mChannel)
                }

                counter++
            } else {
                counter++
            }
        }
    }

    private fun searchFeatureSetGames(featureSet: String): ArrayList<String> {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val categories = ArrayList<String>()
        val mechanics = ArrayList<String>()
        val publisher: String
        val designer: String

        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/featureSets/")

        val jsonText = File(path, "featuresets.json").readText()

        val json = if (jsonText != "") {
            JSONObject(jsonText)
        } else {
            JSONObject()
        }

        val featureSetJson = json.getJSONObject(featureSet)

        for (i in 0 until featureSetJson.getJSONArray("categories").length()) {
            categories.add((featureSetJson.getJSONArray("categories")[i] as JSONObject)
                .getString("id"))
        }

        for (i in 0 until featureSetJson.getJSONArray("mechanics").length()) {
            mechanics.add((featureSetJson.getJSONArray("mechanics")[i] as JSONObject)
                .getString("id"))
        }

        publisher = featureSetJson.getString("publisher")
        designer = featureSetJson.getString("designer")

        var categoriesName = ""
        categories.forEach {
            categoriesName = "$categoriesName$it,"
        }
        if (categoriesName.isNotEmpty())
            categoriesName = categoriesName.substring(0, categoriesName.length - 1)

        var mechanicsName = ""
        mechanics.forEach {
            mechanicsName = "$mechanicsName$it,"
        }
        if (mechanicsName.isNotEmpty())
            mechanicsName = mechanicsName.substring(0, mechanicsName.length - 1)

        val name = "categories=${URLEncoder.encode(categoriesName, "utf-8")}" +
                "&mechanics=${URLEncoder.encode(mechanicsName, "utf-8")}" +
                "&publisher${URLEncoder.encode(publisher, "utf-8")}" +
                "&designer=${URLEncoder.encode(designer, "utf-8")}"

        model.searchGames(name, 0, "feature_set")

        val gamesArray = ArrayList<String>()

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->

                games.forEach {
                    gamesArray.add(it.id!!)
                }
            })

        return gamesArray
    }

    private fun readJson(featureSet: String) : JSONArray {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/featureSets/games/")

        if (!path.exists()) {
            val success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        val jsonText = File(path, "$featureSet.json").readText()

        return if (jsonText != "") {
            JSONObject(jsonText).getJSONArray("games")
        } else {
            JSONArray()
        }
    }

    private fun featureSets() {
        val intent = Intent(this, FeatureSetsActivity::class.java)
        this.startActivity(intent)
    }

    private fun customGameLists() {
        val intent = Intent(this, CustomGameListsActivity::class.java)
        this.startActivity(intent)
    }

    private fun createLocalDirectory() {
        val path = File(this.getExternalFilesDir(null),"/customGameLists/")

        if (!path.exists()) {
            val success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }
    }

    override fun onClick(v: View) {
        pageCounter = 0
        search("name")
        nextButton.isEnabled = true
        previousButton.isEnabled = false
    }

    private fun onNextClick() {
        pageCounter++
        previousButton.isEnabled = true
        search("name")
    }

    private fun onPreviousClick() {
        pageCounter--
        if (pageCounter == 0) {
            previousButton.isEnabled = false
        }
        search("name")
    }

    private fun topTen() {
        pageCounter = 0
        previousButton.isEnabled = false
        nextButton.isEnabled = false
        search("popular")
    }

    // Fetch games function
    private fun search(type: String) {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        val name = txtSearchBoardGameName.text.toString()
        model.searchGames(name, pageCounter, type)

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()

                txtTotalBoardGames.text = games.size.toString()
            })
    }
}
