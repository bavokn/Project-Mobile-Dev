package com.isel.bgg_1

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.isel.bgg_1.adapters.BoardGameAdapter
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import com.isel.bgg_1.notifications.NotificationWorker
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.concurrent.TimeUnit


const val TAG: String = "PDM Project"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model, "")
    }
    private val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }

    private var pageCounter = 0

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
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

        val name = "New Games"
        val descriptionText = "There are new games in one of your feature sets."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("feature_set_update", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        val request = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 30, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())

        val periodicWorkRequest = request.build()
        WorkManager.getInstance().enqueueUniquePeriodicWork("feature_set_update_work",  ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
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
