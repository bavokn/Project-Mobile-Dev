package com.isel.bgg_1.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.isel.bgg_1.R
import com.isel.bgg_1.TAG
import com.isel.bgg_1.boardgameatlas.dto.GameDTO
import kotlinx.android.synthetic.main.spinner_item.view.*
import org.json.JSONObject
import java.io.File


class CustomListSpinnerAdapter(ctx: Context,
                               customGameLists: ArrayList<String>,
                               var currentBoardGame: GameDTO
) :
    ArrayAdapter<String>(ctx, 0, customGameLists) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent, currentBoardGame)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent, currentBoardGame)
    }

    private fun createView(
        position: Int,
        recycledView: View?,
        parent: ViewGroup,
        currentBoardGame: GameDTO
    ): View {
        val customList = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_item,
            parent,
            false
        )
        val JSON = getJSON(parent.context, customList!!)
        val check = checkListContainsGame(JSON, currentBoardGame)
        if (check) {
            view.text.setBackgroundColor(parent.resources.getColor(R.color.md_blue_700))
        } else {
            view.text.setBackgroundColor(parent.resources.getColor(R.color.colorAccent))
        }
        view.text.text = customList.substringBeforeLast(".")
        view.setOnClickListener{
            if (check) {
                removeFromList(JSON, currentBoardGame, customList)
                view.text.setBackgroundColor(parent.resources.getColor(R.color.colorAccent))
            } else {
                addToList(JSON, currentBoardGame, customList)
                view.text.setBackgroundColor(parent.resources.getColor(R.color.md_blue_700))

            }
        }
        return view
    }

    private fun addToList(json: JSONObject, currentBoardGame: GameDTO, customList: String) {
        val tempJsonObject = JSONObject()
        tempJsonObject.put("image_url", currentBoardGame.image_url)
        tempJsonObject.put("name", currentBoardGame.name)
        tempJsonObject.put("year_published", currentBoardGame.year_published)
        tempJsonObject.put("min_players", currentBoardGame.min_players)
        tempJsonObject.put("max_players", currentBoardGame.max_players)
        tempJsonObject.put("min_age", currentBoardGame.min_age)
        tempJsonObject.put("description", currentBoardGame.description)
        tempJsonObject.put("primary_publisher", currentBoardGame.primary_publisher)
        tempJsonObject.put("designers", currentBoardGame.designers)
        tempJsonObject.put("artists", currentBoardGame.artists)
        tempJsonObject.put("average_user_rating", currentBoardGame.average_user_rating)
        tempJsonObject.put("url", currentBoardGame.url)
        json.put(currentBoardGame.id!!, tempJsonObject)
        writeToFile(json, context, customList)
    }

    private fun removeFromList(json: JSONObject, currentBoardGame: GameDTO, customList: String) {
        json.remove(currentBoardGame.id)
        writeToFile(json, context, customList)
    }

    private fun writeToFile(json: JSONObject, context: Context, customList: String) {
        val path = File(context.getExternalFilesDir(null), "/customGameLists/")
        var success = true

        if (!path.exists()) {
            success = path.mkdir()
            Log.d(TAG, "Directory $path was created: $success")
        }

        if (success) {
            Log.d(TAG, "Directory exist, proceed to create the file.")
            val text = json.toString()

            File(path, customList).writeText(text)
        }
    }

    private fun checkListContainsGame(gameList: JSONObject, currentBoardGame: GameDTO): Boolean {
            if (gameList.has(currentBoardGame.id)) {
                return true
            }
        return false
    }

    private fun getJSON(ctx: Context, fileName: String): JSONObject {
        val path = File(ctx.getExternalFilesDir(null),"/customGameLists/")
        val jsonText = File(path, fileName).readText()
        return if (jsonText == "") {
            JSONObject()
        } else {
            JSONObject(jsonText)
        }
    }
}