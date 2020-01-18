package com.isel.bgg_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isel.bgg_1.adapters.*
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.DesignerDTO
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.PublisherDTO
import kotlinx.android.synthetic.main.activity_feature_set_creation.*
import org.json.JSONObject
import java.io.File
import org.json.JSONArray
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.viewmodel.*
import kotlinx.android.synthetic.main.fs_parameter_item.view.*


class FeatureSetCreationActivity : AppCompatActivity() {

    // The parameter mechanics chosen by the user for the new Feature Set
    private val chosenCategories = ArrayList<Any>()
    private val chosenMechanics = ArrayList<Any>()
    private val chosenPublishers = ArrayList<Any>()
    private val chosenDesigners = ArrayList<Any>()

    private val mechanicsModel: FeatureSetMechanicsViewModel by lazy {
        ViewModelProviders.of(this)[FeatureSetMechanicsViewModel::class.java]
    }
    private val categoriesModel: FeatureSetCategoriesViewModel by lazy {
        ViewModelProviders.of(this)[FeatureSetCategoriesViewModel::class.java]
    }
    private val publishersModel: FeatureSetPublishersViewModel by lazy {
        ViewModelProviders.of(this)[FeatureSetPublishersViewModel::class.java]
    }
    private val designersModel: FeatureSetDesignersViewModel by lazy {
        ViewModelProviders.of(this)[FeatureSetDesignersViewModel::class.java]
    }

    private val categoriesAdapter: FSCategoriesAdapter by lazy {
        FSCategoriesAdapter(categoriesModel) {pos: Int, holder: RecyclerView.ViewHolder ->
            addCategoryToFeatureSet(
                pos,
                categoriesModel.games.value?.get(pos), holder
            )
        }
    }

    private fun addCategoryToFeatureSet(
        pos: Int,
        category: ValueDTO?,
        holder: RecyclerView.ViewHolder
    ) {
        when {
            chosenCategories.contains(category!!) -> {
                chosenCategories.remove(category)
                categoriesAdapter.removeCategory(category)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            else -> {
                chosenCategories.add(category)
                categoriesAdapter.addCategory(category)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
        }
    }

    private val mechanicsAdapter: FSMechanicsAdapter by lazy {
        FSMechanicsAdapter(mechanicsModel) { pos: Int, holder: RecyclerView.ViewHolder ->
            addMechanicToFeatureSet(
                pos,
                mechanicsModel.games.value?.get(pos), holder
            )
        }
    }

    private fun addMechanicToFeatureSet(
        pos: Int,
        mechanic: ValueDTO?,
        holder: RecyclerView.ViewHolder
    ) {
        when {
            chosenMechanics.contains(mechanic!!) -> {
                chosenMechanics.remove(mechanic)
                mechanicsAdapter.removeMechanic(mechanic)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            else -> {
                chosenMechanics.add(mechanic)
                mechanicsAdapter.addMechanic(mechanic)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
        }
    }

    private val publishersAdapter: FSPublishersAdapter by lazy {
        FSPublishersAdapter(publishersModel) { pos: Int, holder: RecyclerView.ViewHolder ->
            addPublisherToFeatureSet(
                pos,
                publishersModel.games.value?.get(pos)!!.publishers[0]!!, holder
            )
        }
    }

    private fun addPublisherToFeatureSet(
        pos: Int,
        publisher: String,
        holder: RecyclerView.ViewHolder
    ) {
        when {
            chosenPublishers.contains(publisher) -> {
                chosenPublishers.remove(publisher)
                publishersAdapter.removePublisher(publisher)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            chosenPublishers.size == 0 -> {
                chosenPublishers.add(publisher)
                publishersAdapter.addPublisher(publisher)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
            else -> Toast.makeText(this, "Can only choose one of this feature.", Toast.LENGTH_SHORT).show()
        }
    }

    private val designersAdapter: FSDesignersAdapter by lazy {
        FSDesignersAdapter(designersModel) { pos: Int, holder: RecyclerView.ViewHolder ->
            addDesignerToFeatureSet(
                pos,
                designersModel.games.value?.get(pos)!!.designers[0]!!, holder
            )
        }
    }

    private fun addDesignerToFeatureSet(
        pos: Int,
        designer: String,
        holder: RecyclerView.ViewHolder
    ) {
        when {
            chosenDesigners.contains(designer) -> {
                chosenDesigners.remove(designer)
                designersAdapter.removeDesigner(designer)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            chosenDesigners.size == 0 -> {
                chosenDesigners.add(designer)
                designersAdapter.addDesigner(designer)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
            else -> Toast.makeText(this, "Can only choose one of this feature.", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var json: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_set_creation)

        // Initialize json object
        readJson()

        // Setup recyclerviews
        val categoriesRv = findViewById<RecyclerView>(R.id.categoriesRv)
        categoriesRv.adapter = categoriesAdapter
        categoriesRv.layoutManager = LinearLayoutManager(this)


        val mechanicsRv = findViewById<RecyclerView>(R.id.mechanicsRv)
        mechanicsRv.adapter = mechanicsAdapter
        mechanicsRv.layoutManager = LinearLayoutManager(this)

        val publishersRv = findViewById<RecyclerView>(R.id.publishersRv)
        publishersRv.adapter = publishersAdapter
        publishersRv.layoutManager = LinearLayoutManager(this)

        val designersRv = findViewById<RecyclerView>(R.id.designersRv)
        designersRv.adapter = designersAdapter
        designersRv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.createFsBtn).setOnClickListener {
            createFeatureSet()
        }

        findViewById<Button>(R.id.refreshBtn).setOnClickListener {
            fillRecyclerViews()
        }

        fillRecyclerViews()
    }

    private fun fillRecyclerViews() {
        search("categories")
        search("mechanics")
        search("publisher")
        search("designer")
    }

    private fun createFeatureSet() {
        if (fsNameEdit.text.isNotEmpty()) {
            addToJson()
            this.finish()
        }
    }

    private fun addToJson() {
        val featureSetName = fsNameEdit.text.toString()

        when {
            json.has(featureSetName) -> {
                fsNameEdit.setText("")
                Toast.makeText(this, "A Feature Set with this name already exists.", Toast.LENGTH_LONG).show()
            }
            fsNameEdit.text.toString() == "" -> Toast.makeText(this, "Please enter a name for the Feature Set.", Toast.LENGTH_LONG).show()
            else -> {
                val tempJson = JSONObject()

                val categoryJSONArray = JSONArray()
                chosenCategories.forEach {
                    val categoryJson = JSONObject()
                    categoryJson.put("id", (it as ValueDTO).id)
                    categoryJson.put("name", it.name)
                    categoryJSONArray.put(categoryJson)
                }

                val mechanicJSONArray = JSONArray()

                chosenMechanics.forEach {
                    val mechanicJson = JSONObject()
                    mechanicJson.put("id", (it as ValueDTO).id)
                    mechanicJson.put("name", it.name)
                    mechanicJSONArray.put(mechanicJson)
                }

                var publisherString = ""
                chosenPublishers.forEach {
                    publisherString = it as String
                }

                var designerString = ""
                chosenDesigners.forEach {
                    designerString = it as String
                }
                tempJson.put("categories", categoryJSONArray)
                tempJson.put("mechanics", mechanicJSONArray)
                tempJson.put("publisher", publisherString)
                tempJson.put("designer", designerString)

                json.put(featureSetName, tempJson)

                writeToFile(json)
            }
        }


    }

    private fun writeToFile(json: JSONObject) {
        val path = File(this.getExternalFilesDir(null), "/featureSets/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            val text = json.toString()

            File(path, "featuresets.json").writeText(text)
        }
    }

    private fun readJson() {
        /**
         * Fetch data from local storage
         */
        Log.v(TAG, "**** FETCHING from local storage...")

        val path = File(this.getExternalFilesDir(null),"/featureSets/")
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

    // Fetch games function
    private fun search(type: String) {
        /**
         * Fetch data
         */
        Log.v(TAG, "**** FETCHING from Board Game Atlas...")

        if (type == "categories") {
            categoriesModel.searchGames("", 0, type)
            categoriesModel.games.observe(this,
                Observer<Array<ValueDTO>> { values ->
                    categoriesAdapter.notifyDataSetChanged()
                })
        } else if (type == "mechanics") {
            mechanicsModel.searchGames("", 0, type)
            mechanicsModel.games.observe(this,
                Observer<Array<ValueDTO>> { values ->
                    mechanicsAdapter.notifyDataSetChanged()
                })
        } else if (type == "publisher") {
            publishersModel.searchGames("", 0, type)

            publishersModel.games.observe(this,
                Observer<Array<PublisherDTO>> { games ->
                    publishersAdapter.notifyDataSetChanged()
                })
        } else if (type == "designer") {
            designersModel.searchGames("", 0, type)

            designersModel.games.observe(this,
                Observer<Array<DesignerDTO>> { games ->
                    designersAdapter.notifyDataSetChanged()
                })
        }
    }
}
