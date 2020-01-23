package com.isel.bgg_1.notifications

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.isel.bgg_1.MainActivity
import com.isel.bgg_1.TAG
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    private val model = BoardGamesViewModel(applicationContext as Application)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        // compare current games in json with current games in online database
        compareGames()

        return try {
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun compareGames() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        var counter = 0
        val featureSets = File(applicationContext.getExternalFilesDir(null),"/featureSets/games/")
            .walk().toSet()
        featureSets.forEach {
            if (counter != 0) {
                val featureSet = it.toString().substringAfterLast("/").substringBeforeLast(".")
                val gamesJSONArray = readJson(featureSet)

                val gamesOld = ArrayList<String>()
                for (i in 0 until gamesJSONArray.length()) {
                    gamesOld.add(gamesJSONArray.getString(i))
                }

                val gamesNew = searchFeatureSetGames(featureSet)

                if (gamesNew.size > gamesOld.size) {
                    Log.d("NEW GAME", "New game in $featureSet added.")
                    val builder = NotificationCompat.Builder(applicationContext, "feature_set_update")
                        .setSmallIcon(R.drawable.notification_icon_background)
                        .setContentTitle("New Games!")
                        .setContentText("New game(s) in your $featureSet feature set!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                    with(NotificationManagerCompat.from(applicationContext)) {
                        // notificationId is a unique int for each notification that you must define
                        notify((1..500000).random() + gamesNew.size, builder.build())
                    }
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

        val path = File(applicationContext.getExternalFilesDir(null),"/featureSets/")

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
                "&publisher=${URLEncoder.encode(publisher, "utf-8")}" +
                "&designer=${URLEncoder.encode(designer, "utf-8")}"

        model.searchGames(name, 0, "feature_set")

        val gamesArray = ArrayList<String>()

        model.games.value?.forEach { game ->
            gamesArray.add(game.id!!)
        }

        Thread.sleep(1000)

        return gamesArray
    }

    private fun readJson(featureSet: String): JSONArray {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(applicationContext.getExternalFilesDir(null),"/featureSets/games/")

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
}