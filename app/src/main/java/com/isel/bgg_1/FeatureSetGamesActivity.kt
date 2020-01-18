package com.isel.bgg_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.BoardGameAdapter
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import com.isel.bgg_1.viewmodel.BoardGamesViewModel
import kotlinx.android.synthetic.main.activity_feature_set_games.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

private lateinit var json: JSONObject
private lateinit var categories: ArrayList<String>
private lateinit var mechanics: ArrayList<String>
private lateinit var publisher: String
private lateinit var designer: String

class FeatureSetGamesActivity : AppCompatActivity() {

    private val adapter: BoardGameAdapter by lazy {
        BoardGameAdapter(model, "")
    }
    private val model: BoardGamesViewModel by lazy {
        ViewModelProviders.of(this)[BoardGamesViewModel::class.java]
    }
    private var featureSet = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_set_games)

        featureSet = intent.getStringExtra("feature_set")!!

        featureSetNameTxv.text = featureSet

        deleteFSButton.setOnClickListener {
            deleteFeatureSet(featureSet!!)
        }

        readJson()
        getFeatures(featureSet)

        // Setup recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.featureSetGamesRv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        search()
    }

    private fun deleteFeatureSet(featureSet: String) {
        json.remove(featureSet)
        deleteFeatureSet()
        Toast.makeText(this, "Deleted: $featureSet", Toast.LENGTH_LONG).show()
        this.finish()
    }

    private fun deleteFeatureSet() {
        val path = File(this.getExternalFilesDir(null), "/featureSets/featuresets.json")
        path.writeText(json.toString())
    }

    private fun getFeatures(featureSet: String) {
        val featureSetJson = json.getJSONObject(featureSet)

        categories = ArrayList()
        for (i in 0 until featureSetJson.getJSONArray("categories").length()) {
            categories.add((featureSetJson.getJSONArray("categories")[i] as JSONObject)
                .getString("id"))
        }

        mechanics = ArrayList()
        for (i in 0 until featureSetJson.getJSONArray("mechanics").length()) {
            mechanics.add((featureSetJson.getJSONArray("mechanics")[i] as JSONObject)
                .getString("id"))
        }

        publisher = featureSetJson.getString("publisher")
        designer = featureSetJson.getString("designer")

    }

    private fun readJson() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/featureSets/")

        val jsonText = File(path, "featuresets.json").readText()

        json = if (jsonText != "") {
            JSONObject(jsonText)
        } else {
            JSONObject()
        }
    }

    // Fetch games function
    private fun search() {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

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

        model.games.observe(this,
            Observer<Array<GameDTO>> { games ->
                adapter.notifyDataSetChanged()
                val jsonGames = JSONObject()
                jsonGames.put("featureset", featureSet)
                jsonGames.put("games", JSONArray())
                games.forEach {
                    jsonGames.getJSONArray("games").put(it.id)
                }
                writeToFile(jsonGames)
            })
    }

    private fun writeToFile(json: JSONObject) {
        val path = File(this.getExternalFilesDir(null), "/featureSets/games/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            val text = json.toString()

            File(path, "$featureSet.json").writeText(text)
        }
    }
}
