package com.isel.bgg_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.FeatureSetsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.File

class FeatureSetsActivity : AppCompatActivity() {

    private lateinit var json: JSONObject
    private lateinit var adapter: FeatureSetsAdapter
    private lateinit var featureSets: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_sets)

        readJson()

        featureSets = ArrayList()
        fillFeatureSets()

        val recyclerView = findViewById<RecyclerView>(R.id.featureSetsRv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FeatureSetsAdapter(featureSets) { pos: Int -> showGames(pos,
            featureSets[pos]) }
        recyclerView.adapter = adapter

        // Create new feature set
        findViewById<Button>(R.id.addFeatureSetButton).setOnClickListener {
            createFeatureSet()
        }
    }

    private fun showGames(
        pos: Int,
        featureSet: String
    ) {
        val intent = Intent(this, FeatureSetGamesActivity::class.java)
        intent.putExtra("feature_set", featureSet)
        startActivity(intent)
    }

    override fun onResume()
    {  // After a pause OR at startup
        super.onResume()
        //Refresh your stuff here
        readJson()
        featureSets = ArrayList()
        fillFeatureSets()

        adapter = FeatureSetsAdapter(featureSets) { pos: Int -> showGames(pos,
            featureSets[pos]) }
        featureSetsRv.adapter = adapter
    }

    private fun itemDelete(pos: Int, featureSet: String) {
        Log.d("File removed?", featureSets.remove(featureSet).toString())
        json.remove(featureSet)
        deleteFeatureSet()
        Toast.makeText(this, "Deleted: $featureSet", Toast.LENGTH_LONG).show()
        adapter.notifyItemRemoved(pos)
        adapter.notifyItemRangeChanged(0, featureSets.size)
    }

    private fun deleteFeatureSet() {
        val path = File(this.getExternalFilesDir(null), "/featureSets/featuresets.json")

        path.writeText(json.toString())
    }

    private fun fillFeatureSets() {
        json.keys().forEach {
            featureSets.add(it)
        }
    }

    private fun createFeatureSet() {
        val intent = Intent(this, FeatureSetCreationActivity::class.java)
        this.startActivityForResult(intent, 1)
    }

    private fun readJson() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/featureSets/")

        if (!path.exists()) {
            val success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        val jsonFile = File(path, "featuresets.json")

        if (!jsonFile.exists()) {
            val success = jsonFile.createNewFile()
            Log.d(TAG, "File $jsonFile was created: $success")
        }

        val jsonText = File(path, "featuresets.json").readText()

        json = if (jsonText != "") {
            JSONObject(jsonText)
        } else {
            JSONObject()
        }
    }
}
