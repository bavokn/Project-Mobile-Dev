package com.isel.bgg_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.CustomListsAdapter
import kotlinx.android.synthetic.main.activity_custom_game_lists.*
import org.json.JSONObject
import java.io.File


class CustomGameListsActivity : AppCompatActivity() {

    private var pageCounter: Int = 0
    private lateinit var json: JSONObject
    private lateinit var adapter: CustomListsAdapter
    private lateinit var customGameLists: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_game_lists)

        pageCounter = 0
        readCustomGameLists()

        val recyclerView = findViewById<RecyclerView>(R.id.featureSetsRv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CustomListsAdapter(this, customGameLists) { pos: Int -> itemDelete(pos,
            customGameLists[pos]) }
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.scrollToPosition(0)
            }
        })

        val addListButton = findViewById<Button>(R.id.addFeatureSetButton)

        addListButton.setOnClickListener {
            addList()
        }
    }

    private fun addList() {
        val path = File(this.getExternalFilesDir(null), "/customGameLists/")
        var success = true
        val listName = customGameListTxt.text.toString()
        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to add the file.")
            if (!customGameLists.contains(listName)) {
                File(path, "$listName.json").createNewFile()
                customGameLists.add(listName)
                adapter.notifyItemInserted(customGameLists.size - 1)

            }
        }
    }

    private fun itemDelete(pos : Int, listName: String) {
        Log.d("File removed?", customGameLists.remove(listName).toString())
        deleteListFile(listName)
        Toast.makeText(this, "Deleted: $listName", Toast.LENGTH_LONG).show()
        adapter.notifyItemRemoved(pos)
        adapter.notifyItemRangeChanged(0, customGameLists.size)
    }

    private fun deleteListFile(listName: String) {
        val path = File(this.getExternalFilesDir(null), "/customGameLists/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to delete the file.")

            File(path, listName).delete()
        }
    }

    private fun readCustomGameLists() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")
        customGameLists = ArrayList()
        var counter = 0
        File(this.getExternalFilesDir(null),"/customGameLists/").walk().forEach {
            if (counter != 0) {
                customGameLists.add(it.toString().substringAfterLast("/"))
                counter++
            } else {
                counter++
            }
        }

        Log.d("List: ", customGameLists.toString())
    }
}
